package com.akellolcc.cigars.databases.extensions

import com.akellolcc.cigars.databases.HumidorsTable
import kotlinx.serialization.Serializable

var emptyHumidor = Humidor(-1, "", "", 0, 0)
@Serializable
class Humidor(
    override var rowid: Long,
    var name: String,
    var brand: String,
    var holds: Long,
    var count: Long,
    var temperature: Long? = null,
    var humidity: Double? = null,
    var notes: String? = null,
    var link: String? = null,
    var price: Double? = null,
    var autoOpen: Boolean = false,
    var sorting: Long? = null,
    var type: Long = 0,
) : BaseEntity() {

    constructor(humidor: HumidorsTable) : this(
        humidor.rowid,
        humidor.name,
        humidor.brand,
        humidor.holds,
        humidor.count,
        humidor.temperature,
        humidor.humidity,
        humidor.notes,
        humidor.link,
        humidor.price,
        humidor.autoOpen == true,
        humidor.sorting,
        humidor.type
    )


    /*
        constructor(name: String,
            brand: String?,
            holds: Long,
            temperature: Long = 72,
            humidity: Double = 71.0,
            notes: String? = null,
            link: String? = null,
            autoOpen: Boolean = false,
            sorting: Long = 0,
            type: Long = 0) : this() {
            runDbQuery {
                //Add humidor
                Log.debug("Added Demo Humidor")
                val humidorID = this.dbQuery.transactionWithResult {
                    dbQuery.addHumidor(
                        name,
                        brand,
                        holds,
                        0,
                        temperature,
                        humidity,
                        notes,
                        link,
                        autoOpen,
                        sorting,
                        type
                    )
                    dbQuery.lastInsertRowId().executeAsOne()
                }
                Log.debug("Added Humidor id $humidorID")
                //Add History item to humidor
                val historyID = this.dbQuery.transactionWithResult {
                    dbQuery.addHistory(0, Clock.System.now().toEpochMilliseconds(), 1, 150.0, 0)
                    dbQuery.lastInsertRowId().executeAsOne()
                }
                Log.debug("Added history to id $historyID")
                this.dbQuery.addHistoryToHumidor(humidorID, historyID)
                Log.debug("Added history to humidor")
                load(humidorID)
                Log.debug("Humidor $this.dbObject")
                return@runDbQuery this
            }
        }


        constructor(dbID: Long) : super() {
            load(dbID)
        }
      fun addCigar(cigar: Cigar, count: Long) {
          runDbQuery {
             this.dbQuery.addCigarToHumidor(this.id, cigar.rowid, count)
              this.dbQuery.addHistory(count, Clock.System.now().toEpochMilliseconds(), count, 150.0, 1)
              val historyID = this.dbQuery.lastInsertRowId().executeAsOne()
              this.dbQuery.addHistoryToHumidor(this.id, historyID)
              return@runDbQuery null
          }
      }

      fun addHistory(cigar: History) {
          runDbQuery {
              this.dbQuery.addCigarToHumidor(this.id, cigar.id)
              val historyID = this.dbQuery.transactionWithResult {
                  dbQuery.addHistory(
                      count,
                      Clock.System.now().toEpochMilliseconds(),
                      count,
                      150.0,
                      1
                  )
                  dbQuery.lastInsertRowId().executeAsOne()
              }
              this.dbQuery.addHistoryToHumidor(this.id, historyID)
              return@runDbQuery null
          }
      }

      override fun addHistory(count: Long?, left: Long?, price: Double?,
                              type: Long?, date: Long?): History {
          return runBlocking {
              val history = super.addHistory(count, left, price, type, date)
              this@Humidor.dbQuery.addHistoryToHumidor(this@Humidor.id, history.id)
              return@runBlocking history
          }
      }

      override fun query(id: Long): Flow<HumidorsTable> {
          return this.dbQuery.humidor(id).asFlow().mapToOne(Dispatchers.Default)
      }

      fun update() {

      }
    */
}

