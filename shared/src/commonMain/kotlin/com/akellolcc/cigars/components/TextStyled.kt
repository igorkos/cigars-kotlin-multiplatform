import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.akellolcc.cigars.theme.TextStyles
import com.akellolcc.cigars.theme.textStyle


@Composable
public fun TextStyled(text: String?, style: TextStyles = TextStyles.Headline, maxLines: Int = Int.MAX_VALUE,
                      minLines: Int = 1) {
    val st = textStyle(style)
    Text(
        maxLines = maxLines,
        minLines = minLines,
        text = text ?: "",
        color = st.color,
        fontSize = st.fontSize,
        fontStyle = st.fontStyle,
        fontFamily = st.fontFamily,
        textAlign = st.textAlign
    )
}

