package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel
import com.example.weatherapp.weather.WeatherViewModel

@Composable
fun WeatherApp(viewModel: WeatherViewModel){

    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 60.dp)
    ) {
   val weatherResult = viewModel.weatherResult.observeAsState()

    var city by remember { mutableStateOf("") }

        val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        )

        {
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },


                singleLine = true,
                label = {
                    Text(text = "Search for location")
                },
                shape = RoundedCornerShape(20.dp),
            )
            Spacer(modifier = Modifier.padding(20.dp))
            IconButton(
                onClick = {viewModel.getData(city)
                keyboardController?.hide()}
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search "
                )
            }

        }
        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {CircularProgressIndicator()}
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> { viewModel.getData("New Delhi")

            }
        }
    }

    }
}
@Composable
fun WeatherDetails(data:WeatherModel){
 Card(modifier = Modifier.fillMaxWidth().padding(6.dp),
     colors = CardDefaults.cardColors(
         containerColor = Color(0xFFD4EBA6)
     )

 ){

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
//changes made for  git
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(40.dp )
            )
            Text(text =  data.location.name,
                fontSize =  30.sp )
            Spacer(modifier = Modifier.padding(12.dp))
            Text(text = data.location.country,
                fontSize = 18.sp,
                color = Color.Gray)
        }
        Spacer(modifier = Modifier.padding(16.dp))

        Text(text =  "${data.current.temp_c} °c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
            )

       AsyncImage(

           model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
           contentDescription = "Condition Icon",
           modifier = Modifier.size(160.dp),

       )
        Text(text = data.current.condition.text ,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
    }
    Spacer(modifier = Modifier.padding(12.dp))

    Card() {
        Column (modifier = Modifier.fillMaxWidth()){
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                WeatherKeyValue("Humidity", data.current.humidity)
                WeatherKeyValue("Wind Speed", data.current.wind_kph+"km/h")
            }
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                WeatherKeyValue("pressure", data.current.pressure_in+"Hg")
                WeatherKeyValue("UV", data.current.uv)
            }
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                WeatherKeyValue("Local Time", data.location.localtime)
             //   WeatherKeyValue("Local Date", data.location.lo)
            }

        }
    }

}
@Composable
fun WeatherKeyValue(key:String ,values: String){
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = values, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
//Change
}