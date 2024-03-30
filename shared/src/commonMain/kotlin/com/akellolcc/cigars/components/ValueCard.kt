package com.akellolcc.cigars.components

import TextStyled
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akellolcc.cigars.theme.MaterialColors
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.materialColor

@Composable
fun ValueCard(label: String, value: String?) {
    OutlinedCard(colors = CardDefaults.cardColors(materialColor(MaterialColors.color_primaryContainer),materialColor(MaterialColors.color_onPrimaryContainer)),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.5.dp, materialColor(MaterialColors.color_onPrimaryContainer)),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextStyled(
                label,
                TextStyles.Subhead,
                modifier = Modifier.padding(end = 8.dp)
            )
            TextStyled(
                value,
                TextStyles.Headline,
            )
        }
    }
}

@Composable
fun ValuesCard(label: String? = null, vertical: Boolean = false, content: @Composable () -> Unit) {
    Box {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                materialColor(MaterialColors.color_transparent),
                materialColor(MaterialColors.color_onPrimaryContainer)
            ),
            border = BorderStroke(0.5.dp, materialColor(MaterialColors.color_onPrimaryContainer)),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
        ) {
            if(vertical) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    content()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    content()
                }
            }
        }
        if(label != null) {
            Box(
                modifier = Modifier.padding(start = 24.dp)
                    .background(materialColor(MaterialColors.color_background))
            ) {
                TextStyled(
                    label,
                    TextStyles.Subhead,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
        }
    }
}

