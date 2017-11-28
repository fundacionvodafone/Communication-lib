/*
 * Copyright (c) 2017 , Fundacion Vodafone España
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of copyright holders nor the names of its
   contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDERS OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE. BESIDES THIS, IN NO EVENT FUNDACION VODAFONE ESPAÑA
SHALL BE LIABLE FOR THE REDISTRIBUTION AND USE IN SOURCE AND BINARY FORMS, WITH OR
WITHOUT MODIFICATION, DEVELOPMENTS OR ANY OTHER ACTIONS DONE BY THIRD PARTIES, AND NEITHER
SHALL BE LIABLE FOR ANY DAMAGES (ANY CATEGORY) DERIVED FROM THESE ACTIONS.
Copyright (c) 2013, Fundación Vodafone España
Todos los derechos reservados.

La redistribucion y el uso en las formas de código fuente y binario, con o sin
modificaciones, están permitidos siempre que se cumplan las siguientes condiciones:
1. Las redistribuciones del código fuente deben conservar el aviso de copyright
anterior, esta lista de condiciones y el siguiente descargo de responsabilidad.
2. Las redistribuciones en formato binario deben reproducir el aviso de copyright anterior, esta lista de condiciones y la siguiente renuncia en la documentacion y/u otros materiales suministrados con la distribuci�n.
3. Ni el nombre de los titulares de derechos de autor ni los nombres de sus colaboradores pueden usarse para apoyar o promocionar productos derivados de este software sin permiso previo y por escrito.

ESTE SOFTWARE SE SUMINISTRA POR FUNDACIÓN VODAFONE ESPAÑA ''COMO ESTÁ'' Y
CUALQUIER GARANTÍA EXPRESAS O IMPLÍCITAS, INCLUYENDO, CON CARÁCTER ENUNCIATIVO
PERO NO LIMITATIVO A, LAS GARANTÍAS IMPLÍCITAS DE COMERCIALIZACIÓN Y APTITUD
PARA UN PROPÓSITO DETERMINADO, SON TODAS RECHAZADAS. EN NINGÚN CASO FUNDACIÓN VODAFONE ESPAÑA
SERÁ RESPONSABLE DE DATOS DIRECTOS, INDIRECTOS, INCIDENTALES, ESPECIALES, EJEMPLARES O CONSECUENTES
(INCLUYENDO, CON CARÁCTER ENUNCIATIVO PERO LIMITADO A: LA ADQUISICIÓN DE BIENES O SERVICIOS,LA PÉRDIDA DE USO,
DE DATOS O DE BENEFICIOS O, LA INTERRUPCIÓN DE LA ACTIVIDAD EMPRESARIAL) O POR CUALQUIER OTRO TIPO DE
RESPONSABILIDAD LEGALMENTE ESTABLECIDA, YA SEA POR CONTRATO, RESPONSABILIDAD ESTRICTA O AGRAVIO
(INCLUYENDO NEGLIGENCIA O CUALQUIER OTRA CAUSA) QUE SURJA DE CUALQUIER MANERA DEL USO DE ESTE SOFTWARE,
INCLUSO SI SE HA ADVERTIDO DE LA POSIBILIDAD DE TALES DATOS. EN ESTE MISMO SENTIDO,FUNDACION VODAFONE
ESPAñA NO SERÁ RESPONSABLE BAJO NINGUNA CIRCUNSTANCIA DE LA REDISTRIBUCIÓN Y DEL USO EN LAS FORMAS DE
CÓDIGO FUENTE Y BINARIO QUE TERCEROS HAGAN, DE SUS POSIBLES DESARROLLOS, MODIFICACIONES Y DEMáS ACCIONES,
NI TAMPOCO DE LOS POSIBLES DATOS (DE CUALQUIER CATEGORíA) QUE PUDIERAN GENERARSE A PARTIR DE DICHAS ACCIONES.
 */

package eu.fundacionvf.communication;

/**@usage
 * * <pre>
 * <b>In Manifest xml</b> :
 *   <meta-data
android:name="INVOKE_SERVICE"
android:value="es.fundacionvodafone.amialcance.buttonsinterface.ReceiveService" />

<b>and register the Receiver point to Service</b> :

<receiver android:name="eu.fundacionvf.communication.CommunicationReceiver" >
<intent-filter>
<action android:name="<MYACTION>" />
</intent-filter>
</receiver>
 * </pre>
 * @author Alberto Delgado
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

public class CommunicationReceiver extends BroadcastReceiver
{
  private static final String INVOKE_SERVICE = "INVOKE_SERVICE";
  private Class<?> serviceToInvoke;

  public void onReceive(Context context, Intent intent)
  {
    try
    {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
      Bundle bundle = ai.metaData;

      String className = bundle.getString("INVOKE_SERVICE");
      this.serviceToInvoke = Class.forName(className);
      intent.setClass(context, this.serviceToInvoke);

      context.startService(intent);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
