package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.databases.extensions.Cigar
import com.akellolcc.cigars.logging.Log
import com.akellolcc.cigars.theme.Localize
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

@Composable
fun CigarListRow(entity: Cigar, modifier: Modifier) {
    Log.debug("CigarsListRow ${entity.rowid} ${entity.key}")
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
                text = entity.name,
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
                labelSuffix="",
                text = entity.cigar,
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_length,
                labelSuffix="",
                text = entity.length,
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_gauge,
                labelSuffix="",
                text = "${entity.gauge}",
                style = TextStyles.Subhead,
                vertical = true
            )
            TextStyled(
                label = Localize.cigar_details_count,
                labelSuffix="",
                text = Localize.cigar_list_total(entity.count),
                style = TextStyles.Subhead,
                vertical = true
            )
        }
    }
}