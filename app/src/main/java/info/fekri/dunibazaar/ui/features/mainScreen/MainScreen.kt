package info.fekri.dunibazaar.ui.features.mainScreen

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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.CardViewBackground
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.ui.theme.Shapes

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {

        TopToolbar()

        CategoryBar()

        ProductSubject()
        ProductSubject()

        BigPictureAds()

        ProductSubject()
        ProductSubject()

    }

}

// ----------------------------------------------------------------------

@Composable
fun TopToolbar() {

    TopAppBar(
        backgroundColor = Color.White,
        title = { Text(text = "DuniBazaar") },
        elevation = 0.dp,
        actions = {
            IconButton(onClick = {}) {

                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null
                )

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )

            }
        }
    )

}

// ----------------------------------------------------------------------

@Composable
fun CategoryBar() {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(10) {
            CategoryItem()
        }
    }

}

@Composable
fun CategoryItem() {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = Shapes.medium,
            color = CardViewBackground
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_icon_app),
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
            )
        }
        Text(
            text = "Hotels",
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(color = Color.Gray)
        )
    }
}

// ----------------------------------------------------------------------

@Composable
fun ProductSubject() {

    Column(modifier = Modifier.padding(top = 32.dp)) {
        Text(
            text = "Popular Destinations",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6
        )
        ProductBar()
    }

}

@Composable
fun ProductBar() {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(10) {
            ProductItem()
        }
    }

}

@Composable
fun ProductItem() {
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { },
        elevation = 4.dp,
        shape = Shapes.medium
    ) {

        Column {
            Image(
                painter = painterResource(id = R.drawable.img_intro),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Diamond Women Watches",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                )
                Text(
                    text = "86,000 Tomans",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "154 sold",
                    style = TextStyle(fontSize = 13.sp, color = Color.Gray)
                )
            }
        }

    }
}

// ----------------------------------------------------------------------

@Composable
fun BigPictureAds() {

    Image(
        painter = painterResource(id = R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 22.dp, start = 16.dp, end = 16.dp)
            .clip(Shapes.medium)
            .clickable {  }
    )

}

// ----------------------------------------------------------------------

