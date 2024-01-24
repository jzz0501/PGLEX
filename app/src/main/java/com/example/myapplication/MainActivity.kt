package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mymenu()
        }
    }
}

val db = FirebaseFirestore.getInstance()
val empresas = db.collection("Empresas")
var listaempresa = mutableStateListOf<Empresas>()

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Mymenu() {
    val navController = rememberNavController()
    val estadoDrawer = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = estadoDrawer,
        drawerContent = {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            navController.navigate("pantallainsertar")
                            coroutineScope.launch { estadoDrawer.drawerState.close() }
                        }
                ) {
                    Image(imageVector = Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Insertar", fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            navController.navigate("pantallabuscar")
                            coroutineScope.launch { estadoDrawer.drawerState.close() }
                        }
                ) {
                    Image(imageVector = Icons.Filled.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Buscar", fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            navController.navigate("pantallaModificar")
                            coroutineScope.launch { estadoDrawer.drawerState.close() }
                        }
                ) {
                    Image(imageVector = Icons.Filled.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Modificar", fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            navController.navigate("pantallaborrar")
                            coroutineScope.launch { estadoDrawer.drawerState.close() }
                        }
                ) {
                    Image(imageVector = Icons.Filled.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Borrar", fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable {
                            rellenaLista()
                            navController.navigate("pantallatodos")
                            coroutineScope.launch { estadoDrawer.drawerState.close() }
                        }
                ) {
                    Image(imageVector = Icons.Filled.List, contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Todos", fontSize = 20.sp)
                }
            }
        },
        drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
        topBar = {
            TopAppBar (
                title = {
                    Text(text = "Prueba Firebase")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { estadoDrawer.drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = "pantallainsertar") {
            composable("pantallainsertar") {
                PantallaInsertar()
            }
            composable("pantallabuscar") {
                PantallaBuscar()
            }
            composable("pantallamodificar") {
                PantallaModificar()
            }
            composable("pantallaborrar") {
                PantallaBorrar()
            }
            composable("pantallatodos") {
                PantallaTodos()
            }
        }
    }
}

@Composable
fun PantallaInsertar() {
    val contexto = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla insertar", fontSize = 30.sp)
        var cif by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var web by remember { mutableStateOf("") }
        var trabajadores by remember { mutableStateOf("") }
        OutlinedTextField(
            value = cif,
            onValueChange = { cif = it },
            label = {
                Text(text = "CIF: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(text = "Nombre empresa: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = web,
            onValueChange = { web = it },
            label = {
                Text(text = "web: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = trabajadores,
            onValueChange = { trabajadores = it },
            label = {
                Text(text = "ntrabajadores: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            val empre = Empresas(cif, name, web, trabajadores.toInt())
            empresas.document(cif).set(empre)
            Toast.makeText(contexto, "registro insertado", Toast.LENGTH_LONG).show()
        }) {
            Text(text = "insertar")
        }
    }
}

@Composable
fun PantallaBuscar() {
    val contexto = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla buscar", fontSize = 30.sp)
        var cif by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var web by remember { mutableStateOf("") }
        var trabajadores by remember { mutableStateOf("") }
        OutlinedTextField(
            value = cif,
            onValueChange = { cif = it },
            label = {
                Text(text = "CIF: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        Text(text = "Nombre empresa: $name")
        Text(text = "Pagina web: $web")
        Text(text = "ntrabajadores: $trabajadores")
        Button(onClick = {
            empresas.document(cif).get().addOnSuccessListener {
                if(it.exists()) {
                    cif = it.get("cif").toString()
                    name = it.get("nombre").toString()
                    web = it.get("web").toString()
                    trabajadores = it.get("ntrabajadores").toString()
                    Toast.makeText(contexto, "registro encontrado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(contexto, "registro no encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text(text = "buscar")
        }
    }
}

@Composable
fun PantallaBorrar() {
    val contexto = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla borrar", fontSize = 30.sp)
        var cif by remember { mutableStateOf("") }
        OutlinedTextField(
            value = cif,
            onValueChange = { cif = it },
            label = {
                Text(text = "CIF: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        Button(onClick = {
            empresas.document(cif).get().addOnSuccessListener {
                if(it.exists()) {
                    it.reference.delete()
                    Toast.makeText(contexto, "registro borrado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(contexto, "registro no borrado", Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text(text = "borrar")
        }
    }
}

@Composable
fun PantallaModificar() {
    val contexto = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pantalla insertar", fontSize = 30.sp)
        var cif by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var web by remember { mutableStateOf("") }
        var trabajadores by remember { mutableStateOf("") }
        OutlinedTextField(
            value = cif,
            onValueChange = { cif = it },
            label = {
                Text(text = "CIF: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(text = "Nombre empresa: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = web,
            onValueChange = { web = it },
            label = {
                Text(text = "web: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = trabajadores,
            onValueChange = { trabajadores = it },
            label = {
                Text(text = "ntrabajadores: ")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            empresas.document(cif).set(hashMapOf("cif" to cif, "nombre" to name, "web" to web, "ntrabajadores" to trabajadores.toInt()))
            Toast.makeText(contexto, "registro modificado", Toast.LENGTH_LONG).show()
        }) {
            Text(text = "modificar")
        }
    }
}

fun rellenaLista() {
    listaempresa.clear()
    empresas.get().addOnSuccessListener {
        for (docum in it) {
            val emp = Empresas(
                docum.data.get("cif").toString(),
                docum.data.get("nombre").toString(),
                docum.data.get("web").toString(),
                docum.data.get("ntrabajadores").toString().toInt()
            )
            println("Empresas -> ${docum.id}")
            listaempresa.add(emp)
        }
    }
}

@Composable
fun PantallaTodos() {
    LazyColumn() {
        items(listaempresa) {empres ->
            MostrarEmpresa(empres)
        }
    }
}

@Composable
fun MostrarEmpresa(empres: Empresas) {
    Divider(modifier = Modifier
        .fillMaxWidth()
        .width(16.dp))
    Text(text = empres.cif)
    Text(text = empres.nombre)
    Text(text = empres.web)
    Text(text = empres.ntrabajadores.toString())
    Divider(modifier = Modifier
        .fillMaxWidth()
        .width(16.dp))
}