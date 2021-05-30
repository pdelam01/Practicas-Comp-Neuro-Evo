Práctica elaboración de una neurona analógica.

------------------------------------------
- ENTRENAMIENTO DE UNA NEURONA ANALÓGICA -
------------------------------------------
n = 4; entradas
s = 150; muestras
s1 = 15; muestras para validar
s2 = 135; muestras para entrenar


----------------------------
------- Simulaciones -------
----------------------------
Probar con valores diferentes de: 
	r, Ea1, Ea2, t_max1, t_max2, alfa, a, opcionf
  
------------------------------------
-------- Funciones de salida -------
------------------------------------
Definimos la función de salida f(p):
	if( opcionf == 0) then f(p) := p -> la imagen es (-inf, inf) 
	elseif( opcionf == 1) then f(p) := sen(p) -> la imagen es [-1, 1]
	elseif( opcionf == 2) then f(p) := 2 * 1 / (1 + exp(-p)) - 1 -> la imagen es (-1, 1) 
	elseif( opcionf == 3) then f(p) := 2 * exp(- p ** 2) - 1 -> la imagen es (-1, 1]
