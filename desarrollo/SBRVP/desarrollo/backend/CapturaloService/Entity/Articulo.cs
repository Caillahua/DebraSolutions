using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class Articulo
    {

        public int idArticulo { get; set; }
        public string descripcion { get; set; }
        public string caracteristicas { get; set; }
        public string rutaImagen { get; set; }
        public int idCategoria { get; set; }
        public string NombreCategoria { get; set; }
        public string estado { get; set; }
        public int idTienda { get; set; }
        public int stock { get; set; }
        public decimal precio { get; set; }
        public decimal descuento { get; set; }

        public Articulo()
        {

        }

    }
}