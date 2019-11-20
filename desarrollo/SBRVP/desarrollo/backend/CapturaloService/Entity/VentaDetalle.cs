using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class VentaDetalle
    {
        public int      idVenta             { get; set; }
        public int      idArticulo          { get; set; }
        public string   descripcionArticulo { get; set; }
        public int      cantidad            { get; set; }
        public decimal  monto               { get; set; }
        public decimal  descuento           { get; set; }
        public string   caracteristicas     { get; set; }
        public string   rutaImagen          { get; set; }
    }
}