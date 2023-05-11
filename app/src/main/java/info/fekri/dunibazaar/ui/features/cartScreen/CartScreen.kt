package info.fekri.dunibazaar.ui.features.cartScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.PriceBackground
import info.fekri.dunibazaar.ui.theme.Shapes
import info.fekri.dunibazaar.util.EMPTY_PRODUCT
import info.fekri.dunibazaar.util.stylePrice

@Composable
fun CartScreen() {

    val viewModel = getNavViewModel<CartViewModel>()
    val navigation = getNavController()
    val context = LocalContext.current

}

@Composable
fun CartItem(
    data: Product,
    isChangingNumber: Pair<String, Boolean>,
    onAddItemClicked: (String) -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onItemClicked.invoke(data.productId) },
        elevation = 4.dp,
        shape = Shapes.large
    ) {
        Column {
            AsyncImage(
                model = data.imgUrl, contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = data.name,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From ${data.category} Group",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 14.sp)
                    )

                    Surface(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 6.dp)
                            .clip(Shapes.large),
                        color = PriceBackground
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = stylePrice(
                                (data.price.toInt() * (data.quantity ?: "1").toInt()).toString()
                            ),
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        )
                    }

                }

                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                ) {
                    Card(
                        border = BorderStroke(2.dp, Blue)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            // minus / delete
                            if (data.quantity?.toInt() == 1) {
                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                    )
                                }
                            } else {

                                IconButton(onClick = { onRemoveItemClicked.invoke(data.productId) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_minus),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                    )
                                }

                            }

                            // price - size of product
                            if (isChangingNumber.first == data.productId && isChangingNumber.second) {

                                Text(
                                    text = "...",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                            } else {

                                Text(
                                    text = data.quantity ?: "1",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                            }

                            // add button -> +
                            IconButton(onClick = { onAddItemClicked.invoke(data.productId) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp, start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}