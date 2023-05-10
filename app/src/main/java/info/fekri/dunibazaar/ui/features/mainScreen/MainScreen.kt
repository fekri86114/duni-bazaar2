package info.fekri.dunibazaar.ui.features.mainScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import info.fekri.dunibazaar.model.data.Ads
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.CardViewBackground
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.ui.theme.Shapes
import info.fekri.dunibazaar.util.CATEGORY
import info.fekri.dunibazaar.util.MyScreens
import info.fekri.dunibazaar.util.NetworkChecker
import info.fekri.dunibazaar.util.TAGS
import info.fekri.dunibazaar.util.stylePrice
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain, modifier = Modifier.fillMaxSize()
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    val context = LocalContext.current
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Color.White) }

    val viewModel =
        getNavViewModel<MainViewModel>(parameters = { parametersOf(NetworkChecker(context).isInternetConnected) })
    if (NetworkChecker(context).isInternetConnected) {
        viewModel.loadBadgeNumber()
    }

    val navigation = getNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {

        if (viewModel.showProgress.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(), color = Blue
            )
        }

        TopToolbar(badgeNumber = viewModel.badgeNumber.value, onCartClicked = {
            if (NetworkChecker(context).isInternetConnected) navigation.navigate(MyScreens.CartScreen.route)
            else Toast.makeText(
                context, "Please, check your Internet Connection!", Toast.LENGTH_SHORT
            ).show()
        }, onProfileClicked = {
            navigation.navigate(MyScreens.ProfileScreen.route)
        })

        CategoryBar(CATEGORY) {
            navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
        }

        val productDataState = viewModel.dataProducts
        val adsDataState = viewModel.dataAds
        ProductSubjectList(TAGS, productDataState.value, adsDataState.value) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }

    }

}

// -----------------------------------------------------------------------

@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductItemCLicked: (String) -> Unit
) {

    if (products.isNotEmpty()) {

        Column {

            tags.forEachIndexed { it, _ ->
                val withTagData = products.filter { product -> product.tags == tags[it] }
                ProductSubject(tags[it], withTagData.shuffled(), onProductItemCLicked)

                if (ads.size >= 2) if (it == 1 || it == 2) BigPictureAds(
                    ads[it - 1],
                    onProductItemCLicked
                )

            }

        }

    }

}

// ----------------------------------------------------------------------

@Composable
fun TopToolbar(badgeNumber: Int, onCartClicked: () -> Unit, onProfileClicked: () -> Unit) {

    TopAppBar(elevation = 0.dp,
        backgroundColor = Color.White,
        title = { Text(text = "Duni Bazaar") },
        actions = {

            IconButton(onClick = { onCartClicked.invoke() }) {
                if (badgeNumber == 0) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                } else {
                    BadgedBox(badge = { Badge { Text(text = badgeNumber.toString()) } }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    }
                }
            }

            IconButton(onClick = { onProfileClicked.invoke() }) {
                Icon(Icons.Default.Person, null)
            }

        })

}

// ----------------------------------------------------------------------

@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(categoryList.size) {
            CategoryItem(categoryList[it], onCategoryClicked)
        }
    }

}

@Composable
fun CategoryItem(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onCategoryClicked.invoke(subject.first) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = Shapes.medium, color = CardViewBackground
        ) {
            Image(
                painter = painterResource(id = subject.second),
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
            )
        }
        Text(
            text = subject.first,
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(color = Color.Gray)
        )
    }
}

// ----------------------------------------------------------------------

@Composable
fun ProductSubject(subject: String, data: List<Product>, onProductItemCLicked: (String) -> Unit) {

    Column(modifier = Modifier.padding(top = 32.dp)) {
        Text(
            text = subject,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6
        )
        ProductBar(data, onProductItemCLicked)
    }

}

@Composable
fun ProductBar(data: List<Product>, onProductItemCLicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {
            ProductItem(data[it], onProductItemCLicked)
        }
    }

}

@Composable
fun ProductItem(product: Product, onProductItemCLicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductItemCLicked.invoke(product.productId) },
        elevation = 4.dp,
        shape = Shapes.medium
    ) {

        Column {
            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.name,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                )
                Text(
                    text = stylePrice(product.price),
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "${product.soldItem} Sold",
                    style = TextStyle(fontSize = 13.sp, color = Color.Gray)
                )
            }
        }

    }
}

// ----------------------------------------------------------------------

@Composable
fun BigPictureAds(ads: Ads, onProductItemCLicked: (String) -> Unit) {

    AsyncImage(model = ads.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 22.dp, start = 16.dp, end = 16.dp)
            .clip(Shapes.medium)
            .clickable { onProductItemCLicked.invoke(ads.productId) })

}

// ----------------------------------------------------------------------

