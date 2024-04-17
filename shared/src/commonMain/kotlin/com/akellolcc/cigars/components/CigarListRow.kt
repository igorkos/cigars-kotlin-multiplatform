package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.databases.extensions.BaseEntity
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.databases.extensions.HumidorCigar
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

@Composable
fun CigarListRow(entity: BaseEntity, modifier: Modifier) {
    val cigar = when(entity){
        is Cigar -> entity
        is HumidorCigar -> entity.cigar
        else -> throw IllegalArgumentException("Unknown entity type")
    }
    val total = when(entity){
        is Cigar -> Localize.cigar_list_total(entity.count)
        is HumidorCigar -> Localize.cigar_list_total(entity.count)
        else -> throw IllegalArgumentException("Unknown entity type")
    }


    //Log.debug("CigarsListRow ${entity.rowid} ${entity.key}")
    Card(
        colors = CardDefaults.cardColors(
            containerColor = materialColor(MaterialColors.color_primaryContainer),
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)

        ) {
            TextStyled(
                maxLines = 2,
                minLines = 2,
                text = cigar.name,
                style = TextStyles.Headline,
                keepHeight = true
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            TextStyled(
                label = Localize.cigar_details_shape,
                labelSuffix = "",
                text = cigar.cigar,
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_length,
                labelSuffix = "",
                text = cigar.length,
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_gauge,
                labelSuffix = "",
                text = "${cigar.gauge}",
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_count,
                labelSuffix = "",
                text = total,
                style = TextStyles.Subhead,
                vertical = true
            )
        }
    }
}