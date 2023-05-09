package info.fekri.dunibazaar.ui.features.productScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import info.fekri.dunibazaar.R
import info.fekri.dunibazaar.model.data.Comment
import info.fekri.dunibazaar.model.data.Product
import info.fekri.dunibazaar.ui.features.signUp.MainTextField
import info.fekri.dunibazaar.ui.theme.BackgroundMain
import info.fekri.dunibazaar.ui.theme.Blue
import info.fekri.dunibazaar.ui.theme.MainAppTheme
import info.fekri.dunibazaar.ui.theme.Shapes
import info.fekri.dunibazaar.util.MyScreens
import info.fekri.dunibazaar.util.NetworkChecker

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    MainAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundMain) {
            // ProductScreen()
        }
    }
}

@Composable
fun ProductScreen(productId: String) {
    val context = LocalContext.current

    val viewModel = getNavViewModel<ProductViewModel>()
    viewModel.loadData(productId, NetworkChecker(context).isInternetConnected)

    val navigation = getNavController()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 58.dp)
        ) {

            ProductToolbar(
                productName = "Details",
                badgeNumber = 4,
                OnBackClicked = {
                    navigation.popBackStack()
                },
                OnCartClicked = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "Please, Connect to Internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            ProductItem(
                data = viewModel.thisProduct.value,
                comments = viewModel.comments.value,
                OnCategoryClicked = {
                    navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
                }
            )

        }

        AddToCart()

    }

}

@Composable
fun ProductItem(data: Product, comments: List<Comment>, OnCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        ProductDesign(data = data, OnCategoryClicked)

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp)
        )

        ProductDetail(data, comments.size.toString())

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
        )

        ProductComments(comments) {


        }

    }
}

@Composable
fun CommentBody(comment: Comment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = 0.dp,
        border = BorderStroke(1.dp, Color.LightGray),
        shape = Shapes.large
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = comment.userEmail,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = comment.text,
                style = TextStyle(fontSize = 14.sp)
            )

        }

    }

}

@Composable
fun ProductComments(comments: List<Comment>, AddNewComment: (String) -> Unit) {

    val showCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (comments.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comments",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
            )

            TextButton(
                onClick = {
                    if (NetworkChecker(context).isInternetConnected)
                        showCommentDialog.value = true
                    else
                        Toast.makeText(
                            context,
                            "Please, Connect to Internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            ) {
                Text(
                    text = "Add New Comment",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }

        comments.forEach { CommentBody(it) }
    } else {

        TextButton(
            onClick = {
                if (NetworkChecker(context).isInternetConnected) {
                    showCommentDialog.value = true
                } else {
                    Toast.makeText(context, "Please, Connect to Internet!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) {
            Text(
                text = "Add New Comment",
                style = TextStyle(fontSize = 13.sp)
            )
        }

    }

    if (showCommentDialog.value) {
        // show dialog
        AddNewCommentDialog(
            OnDismiss = { showCommentDialog.value = false },
            OnPositiveClick = {
                AddNewComment.invoke(it)
            }
        )
    }

}

@Composable
fun AddNewCommentDialog(
    OnDismiss: () -> Unit,
    OnPositiveClick: (String) -> Unit
) {

    val context = LocalContext.current
    val userComment = remember { mutableStateOf("") }

    Dialog(onDismissRequest = OnDismiss) {

        Card(
            modifier = Modifier.fillMaxHeight(0.6f),
            elevation = 8.dp,
            shape = Shapes.medium
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Write Your Comment",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // get data from user -->
                MainTextField(
                    edtValue = userComment.value,
                    hint = "Your comment...",
                ) {
                    userComment.value = it
                }

                // buttons -->
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = { OnDismiss.invoke() }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        onClick = {
                            if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {

                                if (NetworkChecker(context).isInternetConnected) {

                                    OnPositiveClick.invoke(userComment.value)
                                    OnDismiss.invoke()

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please, connect to Internet!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "Please, write your Comment!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Ok")
                    }

                }

            }

        }

    }

}

@Composable
fun MainTextField(edtValue: String, hint: String, OnValueChanges: (String) -> Unit) {
    OutlinedTextField(
        value = edtValue,
        onValueChange = OnValueChanges,
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = Shapes.medium,
        placeholder = { Text(text = "Write your comment...") },
        maxLines = 2,
        singleLine = false
    )
}

@Composable
fun ProductDetail(data: Product, commentNumber: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = "$commentNumber Comments",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = data.material,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = "${data.soldItem} Sold",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )

            }

        }

        Surface(
            modifier = Modifier
                .clip(Shapes.large)
                .align(Alignment.Bottom),
            color = Blue
        ) {
            Text(
                text = data.tags, color = Color.White,
                modifier = Modifier.padding(6.dp),
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

    }

}

@Composable
fun ProductDesign(data: Product, OnCategoryClicked: (String) -> Unit) {

    AsyncImage(
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium)
    )
    Text(
        text = data.name,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier.padding(top = 14.dp)
    )

    Text(
        text = data.detailText,
        modifier = Modifier.padding(top = 4.dp),
        style = TextStyle(
            fontSize = 15.sp,
            textAlign = TextAlign.Justify
        )
    )

    TextButton(onClick = { OnCategoryClicked.invoke(data.category) }) {

        Text(
            text = "#${data.category}",
            style = TextStyle(fontSize = 13.sp)
        )

    }

}

@Composable
fun ProductToolbar(
    productName: String,
    badgeNumber: Int,
    OnBackClicked: () -> Unit,
    OnCartClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { OnBackClicked.invoke() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        elevation = 2.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            IconButton(
                onClick = { OnCartClicked.invoke() },
                modifier = Modifier.padding(end = 6.dp),
            ) {
                if (badgeNumber == 0) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                } else {
                    BadgedBox(badge = { Badge { Text(text = badgeNumber.toString()) } }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    }
                }
            }
        }
    )
}

@Composable
fun AddToCart() {
}
