using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class Tienda
    {
        public int? idTienda { get; set; }
        public string rsocial { get; set; }
        public string direccion { get; set; }
        public string ruc { get; set; }
        public bool? delivery { get; set; }
        public bool? ventaOnline { get; set; }
        public bool? recojoTienda { get; set; }
        public int idUsuario { get; set; }
        public string estado { get; set; }

        public Tienda()
        {

        }
    }
}