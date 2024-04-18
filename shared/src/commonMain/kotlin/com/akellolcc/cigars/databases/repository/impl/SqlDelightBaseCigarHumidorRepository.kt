package com.akellolcc.cigars.databases.repository.impl

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.akellolcc.cigars.databases.CigarHumidorTable
import com.akellolcc.cigars.databases.Database
import com.akellolcc.cigars.databases.HumidorCigarsDatabaseQueries
import com.akellolcc.cigars.databases.RepositoryType
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.History
import com.akellolcc.cigars.databases.extensions.HistoryType
import com.akellolcc.cigars.databases.extensions.Humidor
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.databases.repository.CigarHumidorRepository
import com.akellolcc.cigars.databases.repository.CigarsRepository
import com.akellolcc.cigars.databases.repository.HistoryRepository
import com.akellolcc.cigars.databases.repository.HumidorsRepository
import com.akellolcc.cigars.databases.repository.Repository
import com.akellolcc.cigars.databases.repository.impl.queries.CigarHumidorTableQueries
import com.akellolcc.cigars.logging.Log
import com.badoo.reaktive.coroutinesinterop.asObservable
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.doOnBeforeError
import com.badoo.reaktive.observable.flatMap
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableFromFunction
import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.toObservable
import com.badoo.reaktive.observable.wrap
import com.badoo.reaktive.single.SingleWrapper
import com.badoo.reaktive.single.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue

abstract class SqlDelightBaseCigarHumidorRepository(
    protected val queries: HumidorCigarsDatabaseQueries
) : BaseRepository<HumidorCigar>(CigarHumidorTableQueries(queries)), CigarHumidorRepository {

    abstract fun observeAllQuery(): Query<CigarHumidorTable>

    override fun all(
        sortField: String?,
        accenting: Boolean
    ): ObservableWrapper<List<HumidorCigar>> {
        val hRepo = Database.instance.getRepository<HumidorsRepository>(RepositoryType.Humidors)
        val cRepo = Database.instance.getRepository<CigarsRepository>(RepositoryType.Cigars)
        return observeAllQuery().asFlow().mapToList(Dispatchers.IO).map {
            it.map { humidorCigar ->
                val humidor = hRepo.getSync(humidorCigar.humidorId)
                val cigar = (cRepo as Repository<Cigar>).getSync(humidorCigar.cigarId)
                HumidorCigar(humidorCigar.count, humidor, cigar)
            }
        }.asObservable().doOnBeforeError{
            Log.error("Error while getting all humidor cigars $it")
        }.wrap()
    }

    override fun updateCount(entity: HumidorCigar, count: Long, price: Double?, historyType: HistoryType?, humidorTo: Humidor?): ObservableWrapper<HumidorCigar>{
        val c = entity.count - count
        val type = if (c < 0) HistoryType.Addition else HistoryType.Deletion
        var updated : HumidorCigar? = null
        return super.update(entity.copy(count = count)).flatMap {
            updated = it
            val cigarsHistoryDatabase: HistoryRepository = Database.instance.getRepository(RepositoryType.CigarHistory, entity.cigar.rowid)
            Log.info("UpdateCount: Add Cigar History item ${HistoryType.localized(type)}")
            cigarsHistoryDatabase.add(
                History(
                    -1,
                    c.absoluteValue,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price,
                    historyType ?:type,
                    entity.cigar.rowid,
                    entity.humidor.rowid,
                    humidorTo?.rowid
                )
            )
        }.map { updated!! }.doOnBeforeError{
            Log.error("Error while updating humidor cigars count $it")
        }.wrap()
    }

    override fun moveCigar(from: HumidorCigar, to: Humidor, count: Long): ObservableWrapper<Boolean> {
        val humidorsRepository: HumidorsRepository = Database.instance.getRepository(RepositoryType.Humidors)
        var humidorHistoryRepository: HistoryRepository = Database.instance.getRepository(RepositoryType.HumidorHistory, from.humidor.rowid)
        return observable { emitter ->
            if (from.count > count) {
                Log.info("Update count of cigars left in humidor from we move")
                updateCount(from, from.count - count, from.cigar.price, HistoryType.Move).subscribe {
                    emitter.onNext(true)
                }
            } else {
                Log.info("All Cigars is moved from humidor")
                remove(from.cigar, from.humidor).subscribe {
                    emitter.onNext(it)
                }
            }
        }.flatMap {
            val fromEntry = from.humidor.copy(count = from.humidor.count - count)
            Log.info("Update total count of cigars left in humidor from we move $fromEntry")
            humidorsRepository.update(fromEntry)
        }.flatMap {
            val toEntry = find(from.cigar, to)
            if (toEntry == null) {
                Log.info("Add cigar to humidor")
                add(from.cigar, to, count)
            } else {
                Log.info("Update count of cigar we moved to humidor")
                updateCount(toEntry, toEntry.count + count, toEntry.cigar.price, HistoryType.Move)
            }
        }.flatMap {
            Log.info("Update total count of cigars left in humidor from we move")
            humidorsRepository.update(to.copy(count = to.count + count))
        }.flatMap {
            Log.info("Add history item Move From")
            humidorHistoryRepository.add(
                History(
                    -1L,
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price = from.cigar.price,
                    type = HistoryType.MoveFrom,
                    cigarId = from.cigar.rowid,
                    humidorFrom = from.humidor.rowid,
                    humidorTo = to.rowid
                )
            )
        }.flatMap {
            Log.info("Add history item Move To")
            humidorHistoryRepository = Database.instance.getRepository(RepositoryType.HumidorHistory, to.rowid)
            humidorHistoryRepository.add(
                History(
                    -1L,
                    count,
                    Clock.System.now().toEpochMilliseconds(),
                    count,
                    price = from.cigar.price,
                    type = HistoryType.MoveTo,
                    cigarId = from.cigar.rowid,
                    humidorFrom = to.rowid,
                    humidorTo = to.rowid
                )
            )
        }.flatMap {
            Log.info("Finished moving cigar")
            observableOf(true)
        }.doOnBeforeError{
            Log.error("Error while moving cigars $it")
        }.wrap()
    }

    override fun doUpsert(entity: HumidorCigar, add: Boolean): SingleWrapper<HumidorCigar> {
        return singleFromCoroutine {
            if (add) {
                queries.add(
                    entity.count,
                    entity.humidor.rowid,
                    entity.cigar.rowid)
            } else {
                queries.update(
                    entity.count,
                    entity.humidor.rowid,
                    entity.cigar.rowid)
            }
            entity
        }.wrap()
    }

    override fun add(cigar: Cigar, humidor: Humidor, count: Long): ObservableWrapper<HumidorCigar> {
        return super.add(HumidorCigar(count, humidor, cigar))
    }

    override fun remove(cigar: Cigar, from: Humidor): ObservableWrapper<Boolean> {
        return super.remove(from.rowid, cigar.rowid)
    }

    override fun find(cigar: Cigar, humidor: Humidor): HumidorCigar? {
        return super.find(humidor.rowid, cigar.rowid)
    }
}
