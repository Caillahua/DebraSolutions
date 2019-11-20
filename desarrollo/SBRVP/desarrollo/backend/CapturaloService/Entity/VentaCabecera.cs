using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class VentaCabecera
    {
        public int idVenta { get; set; }
        public string fecha { get; set; }
        public int idCliente { get; set; }
        public string nombreCliente { get; set; }
        public int idTienda { get; set; }
        public string nombreTienda { get; set; }
        public decimal total { get; set; }

    }
}   