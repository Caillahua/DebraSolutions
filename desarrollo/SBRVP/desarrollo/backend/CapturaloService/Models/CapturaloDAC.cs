using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using System.Data.SqlClient;
using System.Data.Sql;
using System.Configuration;
using CapturaloService.Entity;


namespace CapturaloService.Models
{
    public class CapturaloDAC
    {

        public Usuario InsertarUsuarioBD(string usuario,string contrasena,string correo, string perfil)
        {
            Object response=0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);

            SqlTransaction transaccion;

            Usuario usuarioobj = new Usuario();

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspInsertarUsuario";
                SqlParameter[] arrParametros = new SqlParameter[4];
                arrParametros[0] = new SqlParameter("@USUARIO", SqlDbType.VarChar, 25);
                arrParametros[0].Value = usuario;
                arrParametros[1] = new SqlParameter("@CONTRASENA", SqlDbType.VarChar, 250);
                arrParametros[1].Value = contrasena;
                arrParametros[2] = new SqlParameter("@CORREO", SqlDbType.VarChar, 250);
                arrParametros[2].Value = correo;
                arrParametros[3] = new SqlParameter("@PERFIL", SqlDbType.VarChar, 1);
                arrParametros[3].Value = perfil;

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddRange(arrParametros);
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;
               
                SqlDataReader reader = cmd.ExecuteReader();

                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        usuarioobj.idUsuario = reader.GetInt32(0);
                        usuarioobj.usuario = reader.GetString(1);
                        usuarioobj.contrasenia = reader.GetString(2);
                        usuarioobj.correo = reader.GetString(3);
                        usuarioobj.estado = reader.GetString(4);
                        usuarioobj.perfil = reader.GetString(5);
                    }
                }
                
                reader.Close();

                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;
            }
            finally
            {
                conn.Close();
            }

            return usuarioobj;
        }


        public Cliente InsertarClienteBD
            (int idusuario,
            string nombre,
            string apellido, 
            string dni,
            string direccion, 
            string rsocial,
            string ruc,
            decimal credito,
            string estado)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Cliente clienteobj= new Cliente();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspInsertarCliente";
                                
                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idusuario;
                cmd.Parameters.Add("@NOMBRE", SqlDbType.VarChar, 100).Value = nombre;
                cmd.Parameters.Add("@APELLIDO", SqlDbType.VarChar, 100).Value = apellido;
                cmd.Parameters.Add("@DNI", SqlDbType.VarChar, 10).Value = dni;
                cmd.Parameters.Add("@DIRECCION", SqlDbType.VarChar, 250).Value = direccion;
                cmd.Parameters.Add("@RSOCIAL", SqlDbType.VarChar, 250).Value = rsocial;
                cmd.Parameters.Add("@RUC", SqlDbType.VarChar, 20).Value = ruc;
                cmd.Parameters.Add("@CREDITO", SqlDbType.Decimal).Value = credito;
                cmd.Parameters.Add("@ESTADO", SqlDbType.VarChar, 2).Value = estado;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;

                using (SqlDataReader reader = cmd.ExecuteReader())
                {

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            clienteobj.idCliente = reader.GetInt32(0);
                            clienteobj.idUsuario = reader.GetInt32(1);
                            clienteobj.nombre = reader.GetString(2);
                            clienteobj.apellido = reader.GetString(3);
                            clienteobj.dni = reader.GetString(4);
                            clienteobj.direccion = reader.GetString(5);
                            clienteobj.rsocial = reader.GetString(6);
                            clienteobj.ruc = reader.GetString(7);
                            clienteobj.credito = reader.GetDecimal(8);
                            clienteobj.estado = reader.GetString(9);
                        }
                    }

                }
                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;
                
            }
            finally
            {
                conn.Close();
            }

            return clienteobj;
        }


        public Autenticacion ValidarCredencialesBD
            (string usuario,
            string contrasena
            )
        {
            Autenticacion response= new Autenticacion();
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);

            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspValidarCredenciales";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@USUARIO", SqlDbType.VarChar,25).Value = usuario;
                cmd.Parameters.Add("@CONTRASENA", SqlDbType.VarChar, 250).Value = contrasena;
                
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;
               // response = cmd.ExecuteScalar();

                using (SqlDataReader reader = cmd.ExecuteReader())
                {

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            response.idUsuario = reader.GetInt32(0);
                           
                        }
                    }

                }
                transaccion.Commit();
                
            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response.idUsuario = -1;
            }
            finally
            {
                conn.Close();
            }

            return response;
        }

        public Cliente ObtenerClienteBD
            (int? idCliente,int? idusuario )
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Cliente clienteobj = new Cliente();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspObtenerCliente";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDCLIENTE", SqlDbType.Int).Value = idCliente;
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idusuario;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;

                
                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            clienteobj.idCliente = reader.GetInt32(0);
                            clienteobj.idUsuario = reader.GetInt32(1);
                            clienteobj.nombre = reader.GetString(2);
                            clienteobj.apellido = reader.GetString(3);
                            clienteobj.dni = reader.GetString(4);
                            clienteobj.direccion = reader.GetString(5);
                            clienteobj.rsocial = reader.GetString(6);
                            clienteobj.ruc = reader.GetString(7);
                            clienteobj.credito = reader.GetDecimal(8);
                            clienteobj.estado = reader.GetString(9);
                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {
                
                conn.Close();
            }

            return clienteobj;
        }



        public Cliente ActualizarClienteBD
            (int idCliente,
            int idusuario,
            string nombre,
            string apellido,
            string dni,
            string direccion,
            string rsocial,
            string ruc,
            decimal credito,
            string estado)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Cliente clienteobj = new Cliente();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspActualizarCliente";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDCLIENTE", SqlDbType.Int).Value = idCliente;
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idusuario;
                cmd.Parameters.Add("@NOMBRE", SqlDbType.VarChar, 100).Value = nombre;
                cmd.Parameters.Add("@APELLIDO", SqlDbType.VarChar, 100).Value = apellido;
                cmd.Parameters.Add("@DNI", SqlDbType.VarChar, 10).Value = dni;
                cmd.Parameters.Add("@DIRECCION", SqlDbType.VarChar, 250).Value = direccion;
                cmd.Parameters.Add("@RSOCIAL", SqlDbType.VarChar, 250).Value = rsocial;
                cmd.Parameters.Add("@RUC", SqlDbType.VarChar, 20).Value = ruc;
                cmd.Parameters.Add("@CREDITO", SqlDbType.Decimal).Value = credito;
                cmd.Parameters.Add("@ESTADO", SqlDbType.VarChar, 2).Value = estado;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            clienteobj.idCliente = reader.GetInt32(0);
                            clienteobj.idUsuario = reader.GetInt32(1);
                            clienteobj.nombre = reader.GetString(2);
                            clienteobj.apellido = reader.GetString(3);
                            clienteobj.dni = reader.GetString(4);
                            clienteobj.direccion = reader.GetString(5);
                            clienteobj.rsocial = reader.GetString(6);
                            clienteobj.ruc = reader.GetString(7);
                            clienteobj.credito = reader.GetDecimal(8);
                            clienteobj.estado = reader.GetString(9);
                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return clienteobj;
        }



        public List<Articulo> buscarArticuloBD(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda )
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Articulo articuloObj;
            List<Articulo> list = new List<Articulo>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspBuscarArticulos";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@NOMBREARTICULO", SqlDbType.VarChar,200).Value = nombreArticulo;
                cmd.Parameters.Add("@IDCATEGORIA", SqlDbType.Int).Value = idCategoria;
                cmd.Parameters.Add("@CARACTERISTICAS", SqlDbType.VarChar,1000).Value = caracteristicas;
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idTienda;
                
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new Articulo();
                            articuloObj.idArticulo = reader.GetInt32(0);
                            articuloObj.descripcion = reader.GetString(1);
                            articuloObj.caracteristicas = reader.GetString(2);
                            articuloObj.rutaImagen = reader.GetString(3);
                            articuloObj.idCategoria = reader.GetInt32(4);
                            articuloObj.NombreCategoria= reader.GetString(5);
                            articuloObj.estado = reader.GetString(6);
                            articuloObj.idTienda = reader.GetInt32(7);
                            articuloObj.stock = reader.GetInt32(8);
                            articuloObj.precio = reader.GetDecimal(9);
                            articuloObj.descuento = reader.GetDecimal(10);
                            list.Add(articuloObj);
                            
                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }

        public List<Articulo> guardarArticuloBD(string descripcion, string caracteristicas, string ruta_imagen, int idcategoria, string estado, int idtienda, int stock, decimal precio, decimal descuento)
        {
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Articulo articuloObj;
            List<Articulo> list = new List<Articulo>();

            SqlTransaction transaccion;

            conn.Open();
            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspInsertarArticulo";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@DESCRIPCION", SqlDbType.VarChar, 200).Value = descripcion;
                cmd.Parameters.Add("@CARACTERISTICAS", SqlDbType.VarChar,1000).Value = caracteristicas;
                cmd.Parameters.Add("@RUTA_IMAGEN", SqlDbType.VarChar, 1000).Value = ruta_imagen;
                cmd.Parameters.Add("@IDCATEGORIA", SqlDbType.Int).Value = idcategoria;
                cmd.Parameters.Add("@ESTADO", SqlDbType.VarChar, 2).Value = estado;
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idtienda;
                cmd.Parameters.Add("@STOCK", SqlDbType.Int).Value = stock;
                cmd.Parameters.Add("@PRECIO", SqlDbType.Decimal).Value = precio;
                cmd.Parameters.Add("@DESCUENTO", SqlDbType.Int).Value = descuento;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new Articulo();
                            articuloObj.idArticulo = reader.GetInt32(0);
                            articuloObj.descripcion = reader.GetString(1);
                            articuloObj.caracteristicas = reader.GetString(2);
                            articuloObj.rutaImagen = reader.GetString(3);
                            articuloObj.idCategoria = reader.GetInt32(4);
                            articuloObj.NombreCategoria = reader.GetString(5);
                            articuloObj.estado = reader.GetString(6);
                            articuloObj.idTienda = reader.GetInt32(7);
                            articuloObj.stock = reader.GetInt32(8);
                            articuloObj.precio = reader.GetDecimal(9);
                            articuloObj.descuento = reader.GetDecimal(10);
                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                

            }
            finally
            {

                conn.Close();
            }

            return list;
        }



        public Tienda InsertarTiendaBD(string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline,bool recojoTienda, int idUsuario, string estado)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Tienda tiendaobj = new Tienda();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspInsertarTienda";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@RSOCIAL", SqlDbType.VarChar,200).Value = rsocial;
                cmd.Parameters.Add("@DIRECCION", SqlDbType.VarChar, 200).Value = direccion;
                cmd.Parameters.Add("@RUC", SqlDbType.VarChar,20).Value = ruc;
                cmd.Parameters.Add("@DELIVERY", SqlDbType.Bit).Value = delivery;
                cmd.Parameters.Add("@VENTAONLINE", SqlDbType.Bit).Value = ventaOnline;
                cmd.Parameters.Add("@RECOJOTIENDA", SqlDbType.Bit).Value = recojoTienda;
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idUsuario;
                cmd.Parameters.Add("@ESTADO", SqlDbType.VarChar,2).Value = estado;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;

                using (SqlDataReader reader = cmd.ExecuteReader())
                {

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            tiendaobj.idTienda = reader.GetInt32(0);
                            tiendaobj.rsocial = reader.GetString(1);
                            tiendaobj.direccion = reader.GetString(2);
                            tiendaobj.ruc = reader.GetString(3);
                            tiendaobj.delivery = reader.GetBoolean(4);
                            tiendaobj.ventaOnline = reader.GetBoolean(5);
                            tiendaobj.recojoTienda = reader.GetBoolean(6);
                            tiendaobj.idUsuario = reader.GetInt32(7);
                            tiendaobj.estado = reader.GetString(8);
                        }
                    }

                }
                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {
                conn.Close();
            }

            return tiendaobj;
        }


        public Tienda ActualizarTiendaBD(int idtienda,string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline, bool recojoTienda, int idUsuario, string estado)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Tienda tiendaobj = new Tienda();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspActualizarTienda";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idtienda;
                cmd.Parameters.Add("@RSOCIAL", SqlDbType.VarChar, 200).Value = rsocial;
                cmd.Parameters.Add("@DIRECCION", SqlDbType.VarChar, 200).Value = direccion;
                cmd.Parameters.Add("@RUC", SqlDbType.VarChar, 20).Value = ruc;
                cmd.Parameters.Add("@DELIVERY", SqlDbType.Bit).Value = delivery;
                cmd.Parameters.Add("@VENTAONLINE", SqlDbType.Bit).Value = ventaOnline;
                cmd.Parameters.Add("@RECOJOTIENDA", SqlDbType.Bit).Value = recojoTienda;
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idUsuario;
                cmd.Parameters.Add("@ESTADO", SqlDbType.VarChar, 2).Value = estado;


                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;

                using (SqlDataReader reader = cmd.ExecuteReader())
                {

                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            tiendaobj.idTienda = reader.GetInt32(0);
                            tiendaobj.rsocial = reader.GetString(1);
                            tiendaobj.direccion = reader.GetString(2);
                            tiendaobj.ruc = reader.GetString(3);
                            tiendaobj.delivery = reader.GetBoolean(4);
                            tiendaobj.ventaOnline = reader.GetBoolean(5);
                            tiendaobj.recojoTienda = reader.GetBoolean(6);
                            tiendaobj.idUsuario = reader.GetInt32(7);
                            tiendaobj.estado = reader.GetString(8);
                        }
                    }

                }
                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {
                conn.Close();
            }

            return tiendaobj;
        }


        public List<Categoria> listarCategoriaBD(int idCategoria, string nombreCategoria)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Categoria articuloObj;
            List<Categoria> list = new List<Categoria>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspListarCategoria";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDCATEGORIA", SqlDbType.Int).Value = idCategoria;
                cmd.Parameters.Add("@DESCRIPCION", SqlDbType.VarChar,50).Value = nombreCategoria;
                
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new Categoria();
                            articuloObj.idCategoria = reader.GetInt32(0);
                            articuloObj.descripcion = reader.GetString(1);
                            articuloObj.estado = reader.GetString(2);
                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }


        public List<Tienda> buscarTiendaBD(int? idTienda, string rsocial, int? idusuario)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Tienda articuloObj;
            List<Tienda> list = new List<Tienda>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspBuscarTienda";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idTienda;
                cmd.Parameters.Add("@RSOCIAL", SqlDbType.VarChar, 250).Value = rsocial;
                cmd.Parameters.Add("@IDUSUARIO", SqlDbType.Int).Value = idusuario;
                //cmd.Parameters.Add("@DELIVERY", SqlDbType.Bit).Value = delivery;
                //cmd.Parameters.Add("@VENTAONLINE", SqlDbType.Bit).Value = ventaOnline;
                //cmd.Parameters.Add("@RECOJOTIENDA", SqlDbType.Bit).Value = recojoTienda;

                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new Tienda();
                            articuloObj.idTienda = reader.GetInt32(0);
                            articuloObj.rsocial = reader.GetString(1);
                            articuloObj.direccion = reader.GetString(2);
                            articuloObj.ruc = reader.GetString(3);
                            articuloObj.delivery = reader.GetBoolean(4);
                            articuloObj.ventaOnline = reader.GetBoolean(5);
                            articuloObj.recojoTienda = reader.GetBoolean(6);
                            articuloObj.idUsuario = reader.GetInt32(7);
                            articuloObj.estado = reader.GetString(8);
                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }


        public Object registrarVentaBD(int idCliente,int idMedioPago,int idArticulo,int idTienda,int cantidad)
        {
            Object response;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);

            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspRegistrarVenta";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDCLIENTE", SqlDbType.Int).Value = idCliente;
                cmd.Parameters.Add("@IDMEDIOPAGO", SqlDbType.Int).Value = idMedioPago;
                cmd.Parameters.Add("@IDARTICULO", SqlDbType.Int).Value = idArticulo;
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idTienda;
                cmd.Parameters.Add("@CANTIDAD", SqlDbType.Int).Value = cantidad;

                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;
                response=cmd.ExecuteScalar();

                

                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response=-1;

            }
            finally
            {

                conn.Close();
            }

            return response;
        }



        public List<VentaCabecera> listarVentaCabecera(int idtienda, int idcliente)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            VentaCabecera articuloObj;
            List<VentaCabecera> list = new List<VentaCabecera>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspListarVentasCabecera";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idtienda;
                cmd.Parameters.Add("@IDCLIENTE", SqlDbType.Int).Value = idcliente;

                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new VentaCabecera();
                            articuloObj.idVenta = reader.GetInt32(0);
                            articuloObj.fecha = reader.GetString(1);
                            articuloObj.idCliente = reader.GetInt32(2);
                            articuloObj.nombreCliente = reader.GetString(3);
                            articuloObj.idTienda = reader.GetInt32(4);
                            articuloObj.nombreTienda = reader.GetString(5);
                            articuloObj.total = reader.GetDecimal(6);
                            
                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }




        public List<VentaDetalle> listarVentaDetalle(int idventa)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            VentaDetalle articuloObj;
            List<VentaDetalle> list = new List<VentaDetalle>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspListarVentaDetalle";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@IDVENTA", SqlDbType.Int).Value = idventa;

                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new VentaDetalle();
                            articuloObj.idVenta = reader.GetInt32(0);
                            articuloObj.idArticulo = reader.GetInt32(1);
                            articuloObj.descripcionArticulo = reader.GetString(2);
                            articuloObj.cantidad = reader.GetInt32(3);
                            articuloObj.monto = reader.GetDecimal(4);
                            articuloObj.descuento = reader.GetDecimal(5);
                            articuloObj.caracteristicas = reader.GetString(6);
                            articuloObj.rutaImagen = reader.GetString(7);

                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }



        public List<Articulo> buscarArticuloBD2(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda, int idarticulo)
        {
            Object response = 0;
            string strcon = ConfigurationManager.ConnectionStrings["ConnectionCapturalo"].ConnectionString;
            SqlConnection conn = new SqlConnection(strcon);


            Articulo articuloObj;
            List<Articulo> list = new List<Articulo>();


            SqlTransaction transaccion;

            conn.Open();

            transaccion = conn.BeginTransaction("EjemploTransaction");

            try
            {

                string sql = "uspBuscarArticulos";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.Add("@NOMBREARTICULO", SqlDbType.VarChar, 200).Value = nombreArticulo;
                cmd.Parameters.Add("@IDCATEGORIA", SqlDbType.Int).Value = idCategoria;
                cmd.Parameters.Add("@CARACTERISTICAS", SqlDbType.VarChar, 1000).Value = caracteristicas;
                cmd.Parameters.Add("@IDTIENDA", SqlDbType.Int).Value = idTienda;
                cmd.Parameters.Add("@IDARTICULO", SqlDbType.Int).Value = idarticulo;

                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Connection = conn;
                cmd.Transaction = transaccion;


                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            articuloObj = new Articulo();
                            articuloObj.idArticulo = reader.GetInt32(0);
                            articuloObj.descripcion = reader.GetString(1);
                            articuloObj.caracteristicas = reader.GetString(2);
                            articuloObj.rutaImagen = reader.GetString(3);
                            articuloObj.idCategoria = reader.GetInt32(4);
                            articuloObj.NombreCategoria = reader.GetString(5);
                            articuloObj.estado = reader.GetString(6);
                            articuloObj.idTienda = reader.GetInt32(7);
                            articuloObj.stock = reader.GetInt32(8);
                            articuloObj.precio = reader.GetDecimal(9);
                            articuloObj.descuento = reader.GetDecimal(10);
                            list.Add(articuloObj);

                        }
                    }
                }


                transaccion.Commit();

            }
            catch (Exception ex)
            {
                transaccion.Rollback();
                response = -1;

            }
            finally
            {

                conn.Close();
            }

            return list;
        }


    }
}