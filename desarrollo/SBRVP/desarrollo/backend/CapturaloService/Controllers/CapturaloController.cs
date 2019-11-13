<<<<<<< HEAD
﻿using CapturaloService.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using CapturaloService.Entity;
using Newtonsoft.Json;
using System.Web.Http.Cors;
using Microsoft.WindowsAzure.Storage.Auth;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using System.IO;
using System.Drawing;
using System.Threading.Tasks;
using System.Diagnostics;

namespace CapturaloService.Controllers
{
    public class CapturaloController : Controller
    {
        // GET: Capturalo
        public ActionResult Index()
        {
            return View();
        }


        public string InsertarUsuario(string usuario, string contrasena, string correo, string perfil)
        {
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Usuario objusu = new Usuario();
            objusu = obj.InsertarUsuarioBD(usuario, contrasena,correo,perfil);
            
            result = JsonConvert.SerializeObject(objusu);

            return result;
        }

        public string InsertarCliente(
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
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.InsertarClienteBD(idusuario,nombre,apellido,dni,
                direccion,rsocial,ruc,credito,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ObtenerCliente(int? idcliente, int? idusuario)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.ObtenerClienteBD(idcliente,idusuario);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ActualizarCliente(
            int idCliente,
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
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.ActualizarClienteBD(idCliente,idusuario,nombre,apellido,dni,direccion,rsocial,ruc,credito,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }


        public string ValidarCredenciales(
            string usuario,
            string contrasena
            )
        {
            
            CapturaloDAC obj = new CapturaloDAC();

            string response = JsonConvert.SerializeObject(obj.ValidarCredencialesBD(usuario,contrasena));


       
            return response;
        }


        public string BuscarArticulo(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            objcli = obj.buscarArticuloBD( nombreArticulo,  idCategoria,  caracteristicas,  idTienda);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string BuscarArticulo2(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda, int idarticulo)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            objcli = obj.buscarArticuloBD2(nombreArticulo, idCategoria, caracteristicas, idTienda, idarticulo);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string guardarArticulo(string descripcion, string caracteristicas, string ruta_imagen, int idcategoria, string estado, int idtienda, int stock, decimal precio, decimal descuento)
        {

            string result = "";
            string urlImagen = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            //urlImagen = UploadImage_URL(ruta_imagen, nombreArchivo);

            objcli = obj.guardarArticuloBD( descripcion,  caracteristicas, ruta_imagen,  idcategoria,  estado,  idtienda,  stock,  precio,  descuento);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }


        public string InsertarTienda(string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline, bool recojoTienda, int idUsuario, string estado)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Tienda objcli = new Tienda();
            objcli = obj.InsertarTiendaBD(rsocial,direccion,ruc,delivery,ventaOnline,recojoTienda,idUsuario,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ActualizarTienda(int idtienda, string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline, bool recojoTienda, int idUsuario, string estado)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Tienda objcli = new Tienda();
            objcli = obj.ActualizarTiendaBD(idtienda,rsocial, direccion, ruc, delivery, ventaOnline, recojoTienda, idUsuario, estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string listarcategoria(int idCategoria,string descripcion)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Categoria> objcli = new List<Categoria>();
            objcli = obj.listarCategoriaBD(idCategoria,descripcion);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string buscarTienda(int? idTienda, string rsocial, int? idusuario)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Tienda> objcli = new List<Tienda>();
            objcli = obj.buscarTiendaBD(idTienda,rsocial,idusuario);

            

            result = JsonConvert.SerializeObject(objcli);

            if (objcli.Count == 0)
            {
                result = "";
            }

            return result;
        }

        public string registraVenta(int idCliente, int idMedioPago, int idArticulo, int idTienda, int cantidad)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Object objcli = new Object();
            objcli = obj.registrarVentaBD(idCliente, idMedioPago,idArticulo,idTienda,cantidad);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string listarVentaCabecera(int idtienda, int idcliente)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<VentaCabecera> objlist = new List<VentaCabecera>();
            objlist = obj.listarVentaCabecera(idtienda, idcliente);

            result = JsonConvert.SerializeObject(objlist);

            return result;
        }


        public string listarVentaDetalle(int idventa)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<VentaDetalle> objlist = new List<VentaDetalle>();
            objlist = obj.listarVentaDetalle(idventa);

            result = JsonConvert.SerializeObject(objlist);

            return result;
        }



        public string UploadImage_URL(string url, string nombre)
        {
            string urlImagen = "";
            string accountname = "storagecapturalo";

            string accesskey = "rkWTKn3CJXGBQiWIjII4/5WCn5rrgZfqXSjHlVaGmGjigpI5LMHNOeDfscCpgo5PgTpHXNyaYd0fd9m/Yu9GTQ==";



            StorageCredentials creden = new StorageCredentials(accountname, accesskey);

            CloudStorageAccount acc = new CloudStorageAccount(creden, useHttps: true);

            CloudBlobClient client = acc.CreateCloudBlobClient();

            CloudBlobContainer cont = client.GetContainerReference("storagecapturalo");

            cont.CreateIfNotExists();

            cont.SetPermissions(new BlobContainerPermissions
            {
                PublicAccess = BlobContainerPublicAccessType.Blob

            });

            CloudBlockBlob cblob = cont.GetBlockBlobReference(nombre+DateTime.Now.ToString().Replace("/","-")+".jpg");

            using (Stream file = System.IO.File.OpenRead(url))

            {
                cblob.UploadFromStream(file);
                urlImagen = cblob.Uri.AbsoluteUri;

            }

            return urlImagen;

        }



    }
=======
﻿using CapturaloService.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using CapturaloService.Entity;
using Newtonsoft.Json;
using System.Web.Http.Cors;
using Microsoft.WindowsAzure.Storage.Auth;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using System.IO;
using System.Drawing;
using System.Threading.Tasks;
using System.Diagnostics;

namespace CapturaloService.Controllers
{
    public class CapturaloController : Controller
    {
        // GET: Capturalo
        public ActionResult Index()
        {
            return View();
        }


        public string InsertarUsuario(string usuario, string contrasena, string correo, string perfil)
        {
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Usuario objusu = new Usuario();
            objusu = obj.InsertarUsuarioBD(usuario, contrasena,correo,perfil);
            
            result = JsonConvert.SerializeObject(objusu);

            return result;
        }

        public string InsertarCliente(
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
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.InsertarClienteBD(idusuario,nombre,apellido,dni,
                direccion,rsocial,ruc,credito,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ObtenerCliente(int? idcliente, int? idusuario)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.ObtenerClienteBD(idcliente,idusuario);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ActualizarCliente(
            int idCliente,
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
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Cliente objcli = new Cliente();
            objcli = obj.ActualizarClienteBD(idCliente,idusuario,nombre,apellido,dni,direccion,rsocial,ruc,credito,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }


        public string ValidarCredenciales(
            string usuario,
            string contrasena
            )
        {
            
            CapturaloDAC obj = new CapturaloDAC();

            string response = JsonConvert.SerializeObject(obj.ValidarCredencialesBD(usuario,contrasena));


       
            return response;
        }


        public string BuscarArticulo(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            objcli = obj.buscarArticuloBD( nombreArticulo,  idCategoria,  caracteristicas,  idTienda);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string BuscarArticulo2(string nombreArticulo, int idCategoria, string caracteristicas, int idTienda, int idarticulo)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            objcli = obj.buscarArticuloBD2(nombreArticulo, idCategoria, caracteristicas, idTienda, idarticulo);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string guardarArticulo(string descripcion, string caracteristicas, string ruta_imagen, int idcategoria, string estado, int idtienda, int stock, decimal precio, decimal descuento)
        {

            string result = "";
            string urlImagen = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Articulo> objcli = new List<Articulo>();
            //urlImagen = UploadImage_URL(ruta_imagen, nombreArchivo);

            objcli = obj.guardarArticuloBD( descripcion,  caracteristicas, ruta_imagen,  idcategoria,  estado,  idtienda,  stock,  precio,  descuento);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }


        public string InsertarTienda(string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline, bool recojoTienda, int idUsuario, string estado)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Tienda objcli = new Tienda();
            objcli = obj.InsertarTiendaBD(rsocial,direccion,ruc,delivery,ventaOnline,recojoTienda,idUsuario,estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string ActualizarTienda(int idtienda, string rsocial, string direccion, string ruc, bool delivery, bool ventaOnline, bool recojoTienda, int idUsuario, string estado)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Tienda objcli = new Tienda();
            objcli = obj.ActualizarTiendaBD(idtienda,rsocial, direccion, ruc, delivery, ventaOnline, recojoTienda, idUsuario, estado);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string listarcategoria(int idCategoria,string descripcion)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Categoria> objcli = new List<Categoria>();
            objcli = obj.listarCategoriaBD(idCategoria,descripcion);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string buscarTienda(int? idTienda, string rsocial, int? idusuario)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<Tienda> objcli = new List<Tienda>();
            objcli = obj.buscarTiendaBD(idTienda,rsocial,idusuario);

            

            result = JsonConvert.SerializeObject(objcli);

            if (objcli.Count == 0)
            {
                result = "";
            }

            return result;
        }

        public string registraVenta(int idCliente, int idMedioPago, int idArticulo, int idTienda, int cantidad)
        {
            
            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            Object objcli = new Object();
            objcli = obj.registrarVentaBD(idCliente, idMedioPago,idArticulo,idTienda,cantidad);

            result = JsonConvert.SerializeObject(objcli);

            return result;
        }

        public string listarVentaCabecera(int idtienda, int idcliente)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<VentaCabecera> objlist = new List<VentaCabecera>();
            objlist = obj.listarVentaCabecera(idtienda, idcliente);

            result = JsonConvert.SerializeObject(objlist);

            return result;
        }


        public string listarVentaDetalle(int idventa)
        {

            string result = "";
            CapturaloDAC obj = new CapturaloDAC();
            List<VentaDetalle> objlist = new List<VentaDetalle>();
            objlist = obj.listarVentaDetalle(idventa);

            result = JsonConvert.SerializeObject(objlist);

            return result;
        }



        public string UploadImage_URL(string url, string nombre)
        {
            string urlImagen = "";
            string accountname = "storagecapturalo";

            string accesskey = "rkWTKn3CJXGBQiWIjII4/5WCn5rrgZfqXSjHlVaGmGjigpI5LMHNOeDfscCpgo5PgTpHXNyaYd0fd9m/Yu9GTQ==";



            StorageCredentials creden = new StorageCredentials(accountname, accesskey);

            CloudStorageAccount acc = new CloudStorageAccount(creden, useHttps: true);

            CloudBlobClient client = acc.CreateCloudBlobClient();

            CloudBlobContainer cont = client.GetContainerReference("storagecapturalo");

            cont.CreateIfNotExists();

            cont.SetPermissions(new BlobContainerPermissions
            {
                PublicAccess = BlobContainerPublicAccessType.Blob

            });

            CloudBlockBlob cblob = cont.GetBlockBlobReference(nombre+DateTime.Now.ToString().Replace("/","-")+".jpg");

            using (Stream file = System.IO.File.OpenRead(url))

            {
                cblob.UploadFromStream(file);
                urlImagen = cblob.Uri.AbsoluteUri;

            }

            return urlImagen;

        }



    }
>>>>>>> origin/chacaliazaDebra
}