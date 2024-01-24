package com.example.myapplication

data class Empresas(val cif: String, val nombre: String, val web: String, val ntrabajadores: Int) {
    override fun toString(): String {
        return "Empresas(cif='$cif', nombre='$nombre', web='$web', ntrabajadores=$ntrabajadores)"
    }
}
