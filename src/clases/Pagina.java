/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import principal.Principal;

/**
 *
 * @author vbarrera
 */
public class Pagina implements Printable {

        //Se obtienen las lineas de texto del JTextArea, la linea de texto finaliza cuando se encuentra el caracter de nueva linea \n
        StringTokenizer lineasdetexto = new StringTokenizer("", "\n", true);
        //Se obtiene el total de lineas de texto
        int totallineas = lineasdetexto.countTokens();

        int[] paginas;  // Arreglo de número de paginas que se necesitaran para imprimir todo el texto 

        String[] textoLineas; //Lineas de texto que se imprimiran en cada hoja

        //Metodo que se crea por default cuando una clase implementa a Printable
        public int print(Graphics g, PageFormat pf, int pageIndex)
                throws PrinterException {
            //Se establece la fuente, el tipo, el tamaño, la metrica según la fuente asignada, 
            //obtiene la altura de cada linea de texto para que todas queden iguales
            Font font = new Font("Serif", Font.PLAIN, 8);
            FontMetrics metrics = g.getFontMetrics(font);
            int altodelinea = metrics.getHeight();
            //Calcula el número de lineas por pagina y el total de paginas
            if (paginas == null) {
                initTextoLineas();
                //Calcula las lineas que le caben a cada página dividiendo la altura imprimible entre la altura de la linea de texto
                int lineasPorPagina = (int) (pf.getImageableHeight() / altodelinea);
                //Calcula el numero de páginas dividiendo el total de lineas entre el numero de lineas por página
                int numeroPaginas = (textoLineas.length - 1) / lineasPorPagina;
                paginas = new int[numeroPaginas];
                for (int b = 0; b < numeroPaginas; b++) {
                    paginas[b] = (b + 1) * lineasPorPagina;
                }
            }
            //Si se recibe un indice de página mayor que el total de páginas calculadas entonces 
            //retorna NO_SUCH_PAGE para indicar que tal pagina no existe 
            if (pageIndex > paginas.length) {
                return NO_SUCH_PAGE;
            }
            /*Por lo regular cuando dibujamos algun objeto lo coloca en la coordenada (0,0), esta coordenada 
             * se encuentra fuera del área imprimible, por tal motivo se debe trasladar la posicion de las lineas de texto
             * según el área imprimible del eje X y el eje Y 
             */

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            /*Dibujamos cada línea de texto en cada página,
             * se aumenta a la posición 'y' la altura de la línea a cada línea de texto para evitar la saturación de texto 
             */

            int y = 0;
            int start = (pageIndex == 0) ? 0 : paginas[pageIndex - 1];
            int end = (pageIndex == paginas.length) ? textoLineas.length : paginas[pageIndex];
            for (int line = start; line < end; line++) {
                y += altodelinea;
                g.drawString(textoLineas[line], 0, y);
            }
            /* Retorna PAGE_EXISTS para indicar al invocador que esta página es parte del documento impreso
             */
            return PAGE_EXISTS;
        }

        /* Agrega las lineas de texto al arreglo */
        public void initTextoLineas() {
            if (textoLineas == null) {
                int numLineas = totallineas;
                textoLineas = new String[numLineas];
                //Se llena el arreglo que contiene todas las lineas de texto
                while (lineasdetexto.hasMoreTokens()) {
                    for (int i = 0; i < numLineas; i++) {
                        textoLineas[i] = lineasdetexto.nextToken();
                    }
                }
            }
        }

        //Este metodo crea un objeto Printerjob el cual es inicializado y asociado con la impresora por default
        public void imprimirnomina() {

            try {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(this);
                //Si el usuario presiona imprimir en el dialogo de impresión,
                //entonces intenta imprimir todas las lineas de texto
                String printName = "EPSON TM-U220";
                //String printName = "Microsoft XPS Document Writer";
//aqui creo un arreglo para obtener todas las impresoras que tengo instaladas
                PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                
                //aqui le digo con que impresora voy a trabajar
                AttributeSet aset = new HashAttributeSet();
                aset.add(new PrinterName(printName, null));
                services = PrintServiceLookup.lookupPrintServices(null, aset);
                job.setPrintService(services[0]);
                job.setJobName("Cliente");
                job.print();
                // boolean ok = job.printDialog();
//         if (ok) {
//             try {
//                 
//                  job.print();
//             } catch (PrinterException ex) {
//              /* The job did not successfully complete */
//             }
            } catch (PrinterException ex) {
                Logger.getLogger(Pagina.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
}

