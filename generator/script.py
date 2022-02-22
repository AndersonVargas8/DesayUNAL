from unittest import result
import mysql.connector
import json
import random


data_file = open("generator/data.json",)
data = json.load(data_file)

mydb = mysql.connector.connect(
    host = "localhost",
    user = "root",
    password = "1234",
    database = "desayunal",
    port = 3307
)

cursor = mydb.cursor()

def crearUsuario():
    estado = "Activo"
    role = "Cliente"
    vals = []

    #primero un admin
    v = ("meluk","12345","activo","Administrador")
    sql = "INSERT INTO usuario (user_name, password, estado, role) VALUES (%s, %s, %s, %s);"
    mydb.commit()

    cursor.execute(sql, v)
    for i in range (200): ##crear 2000 usuarios

        user = data["random_names"][i]
        password = random.choice(data["random_passwords"])
        tupla = (user,password,estado,role)
        vals.append(tupla)
        
        sql = "INSERT INTO usuario (user_name, password, estado, role) VALUES (%s, %s, %s, %s);"
        cursor.execute(sql, tupla)
        
        if i%100==0:
            print("se han insertado: "+ str(i) + " usuarios")

    mydb.commit()

def crearProductos():
    nombres = ["changua","cafe en leche", "chocolate","Arepa con queso","Croissant","Huevo revuelto","Sandwich","Huevos fritos","Torta de Chocolate","Galleta de chocolate","Caldo","Tamal","Hojaldre"]
    precios = [5000,1200,2000,2000,1000,3200,1700,700,2500,1500,4000,3500,400]
    for i in range(len(nombres)):
        values = ( "Desayuno", "rico","Disponible",nombres[i],precios[i] )

        sql = "INSERT INTO producto (categoria, descripcion, estado, nombre,precio) VALUES (%s, %s, %s, %s,%s);"
        cursor.execute(sql, values)
    
    mydb.commit()

def generarCantidades(n):
    l =[]
    for i in range(n):
        l.append( random.randint(1,8))
    return l

def traerProductos():
    sql= "SELECT * FROM producto;"
    cursor.execute(sql)
    return  cursor.fetchall()


def crearOrdenes():

    lista_productos = traerProductos()
    print("\n lista de todos: \n "+str(lista_productos))

    sql = "SELECT COUNT(*) FROM ORDEN;"
    cursor.execute(sql) 
    n_ord = cursor.fetchall()[0][0]
    conta = 1

    
    for i in range(n_ord+1, n_ord + 10000): #10.000 ordenes
        precio = 0
        subtotales = []

        dia= random.randint(1,28)
        mes = random.randint(1,12) 
        anio = random.choice([2020,2021,2022])
        estado = "Entregado"
        usuario = random.randint(1,201)
        hora_entrega = "11:59:10"
        hora_pedido = random.randint(6,21) #hora entre 6am y 9 pm

        min_pedido = random.randint(1,50)
        
        if min_pedido<10:
            min_pedido = "0"+str(min_pedido)

        #n_productos = 2
        n_productos = random.randint(1,8) #numero de productos
        cantidades = generarCantidades(n_productos)

        #produc_compra = random.sample(lista_productos,2)  #asumiendo que hay 25 productos seleccionar n_productos
        produc_compra = random.sample(lista_productos,n_productos)  


        for k in range(n_productos):
            precio+= cantidades[k]* produc_compra[k][5]
            subtotales.append(cantidades[k]* produc_compra[k][5])


        sql = "INSERT INTO orden (dia,mes,anio,estado,hora_entrega,hora_pedido,min_pedido,precio,fk_usuarioid) VALUES (%(dia)s,%(mes)s,%(anio)s,%(estado)s,%(hora_entrega)s,%(hora_pedido)s,%(min_pedido)s,%(precio)s,%(usuario)s)"
        
        values = {
                "dia" : dia,
                "mes" : mes,
                "anio" : anio,
                "estado": estado,
                "hora_entrega":hora_entrega,
                "hora_pedido":hora_pedido,
                "min_pedido" : min_pedido,
                "precio":precio,
                "usuario":usuario
                }
        
        #values = (estado,fecha, hora_entrega, hora_pedido,int(precio), int(usuario))
        cursor.execute(sql,values)
        mydb.commit()

        if(conta%100 == 0):
            print("se han creado "+ str(i) + "órdenes")
        
        conta+=1
        crearDetalleOrden(i,cantidades,produc_compra,subtotales)


def  crearDetalleOrden(i,cantidades,produc_compra,subtotales):

    for k in range(len(subtotales)):
        values = {
        "cantidad_producto" : cantidades[k],
        "subtotal" : subtotales[k],
        "fk_ordenid" : i,
        "productoid" : produc_compra[k][0]
        }

        sql = "INSERT INTO DETALLES_ORDEN (cantidad_producto,subtotal,fk_ordenid,fk_productoid)  VALUES (%(cantidad_producto)s,%(subtotal)s,%(fk_ordenid)s,%(productoid)s);"
        cursor.execute(sql,values)
        mydb.commit()


crearUsuario()
crearProductos()
crearOrdenes()


