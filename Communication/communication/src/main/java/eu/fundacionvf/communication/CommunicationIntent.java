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


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
/**
 * @author Alberto Delgado
 */
public class CommunicationIntent extends Intent {
    private static final String EXTRA_PARAMS = "params";
    private static final String EXTRA_EVENT = "idEvent";
    private static final String EXTRA_ACTION = "idAction";
    private static final String EXTRA_ORIGIN_PACKAGE = "packageorigin";
    private static final String password = BuildConfig.SECRET_KEY;

    protected String params = "";
    private JSONObject json;

    /**
     * Build a secure CommunicationIntent based on the Intent received.
     *
     * @param cntx the current app context
     * @param inte The origin intent received
     * @return CommunicationIntent, the CommunicationIntent instance.
     */
    public static CommunicationIntent intentToCommunicationIntent(Context cntx,
                                                                  Intent inte) {
        try {
            int evento = inte.getIntExtra(EXTRA_EVENT, 0);
            String action = inte.getStringExtra(EXTRA_ACTION);
            String origin = inte.getStringExtra(EXTRA_ORIGIN_PACKAGE);

            CommunicationIntent i;

            if ((evento == 0) && (action.length() < 1)) {
                i = null;
            } else {
                i = new CommunicationIntent(cntx, inte.getAction(), evento,
                        action);
                String encrypparams = inte.getStringExtra(EXTRA_PARAMS);
                String auxparams = SimpleCryptoUtil.decrypt(password,
                        encrypparams);

                i.setStringParams(auxparams);
                if (origin != null) {
                    i.setOriginPackage(origin);
                }
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();


        }
        return null;
    }


    /**
     * CommunicationIntent Builder.
     *
     * @param cntx     Current App Context
     * @param intentFilter   InterFiler action which the BroadcastReceiver should be
     *                 listened, Usually "Orchestrator"
     * @param idEvento The id Event, usually the id Event received + 1
     * @param action Should be the same that the received
     */
    public CommunicationIntent(Context cntx, String intentFilter, int idEvento,
                               String action) {
        super(intentFilter);
        if (Build.VERSION.SDK_INT < 12) {
            setFlags(268435464);
        } else {
            setFlags(268435496);
        }
        // Log.d("origin in Intent", cntx.getPackageName());
        putExtra(EXTRA_ORIGIN_PACKAGE, cntx.getPackageName());
        putExtra(EXTRA_EVENT, idEvento);
        putExtra(EXTRA_ACTION, action);
    }

    public int getIdEvent() {
        return getIntExtra(EXTRA_EVENT, 0);
    }


    /**
     * Method to obtain who is the owner of CommunicationIntent
     *
     * @return the  app owner package
     */
    public String getOriginPackage() {
        return getStringExtra(EXTRA_ORIGIN_PACKAGE);
    }

    private void setOriginPackage(String origin) {
        putExtra(EXTRA_ORIGIN_PACKAGE, origin);
    }

    /**
     * @return int  idAction of the event.
     */
    public String getIdAction() {
        return getStringExtra(EXTRA_ACTION);
    }

    /**
     * Add a key/value to the JSON String
     *
     * @param id    the key value
     * @param value the value itself
     * @return boolean "true" if put successfully , "false" if not
     */
    public boolean setParams(String id, String value) {
        try {
            if (this.json == null) {
                writeJson(id, value);
            } else {
                writeNewJsonObject(this.json, id, value);
            }
            this.params = this.json.toString();
            putExtra(EXTRA_PARAMS, this.params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setStringParams(String params) {
        try {
            if (params.length() > 0) {
                JSONObject str = new JSONObject(params);

                JSONArray root = str.getJSONObject("jsonfile").getJSONArray(
                        EXTRA_PARAMS);

                for (int i = 0; i < root.length(); i++) {
                    JSONObject obj = root.getJSONObject(i);
                    setParams(obj.getString("id"), obj.getString("value"));
                }
            } else {
                this.params = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeJson(String id, String value) throws JSONException {
        this.json = new JSONObject();
        JSONObject jsonObjectParams = new JSONObject();
        JSONObject jsonObjectData = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObjectData.put("id", id);
        jsonObjectData.put("value", value);

        jsonArray.put(jsonObjectData);

        jsonObjectParams.put(EXTRA_PARAMS, jsonArray);
        this.json.put("jsonfile", jsonObjectParams);
    }

    private void writeNewJsonObject(JSONObject json, String id, String value)
            throws JSONException {
        JSONObject jsonObjectNewData = new JSONObject();

        jsonObjectNewData.put("id", id);
        jsonObjectNewData.put("value", value);
        json.getJSONObject("jsonfile").getJSONArray(EXTRA_PARAMS)
                .put(jsonObjectNewData);
    }

    /**
     * Obtain the keys list of CommunicationIntent
     *
     * @return String[] the Array with all JSON String ids if not happen any
     * problem, null in other case.
     */

    public String[] getArrayIds() {
        try {
            JSONArray jsonArray = this.json.getJSONObject("jsonfile")
                    .getJSONArray(EXTRA_PARAMS);
            String[] arrayIds = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject result = jsonArray.getJSONObject(i);

                arrayIds[i] = result.getString("id");
            }

            return arrayIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtain the value based on the key
     *
     * @param id the value key to find the value
     * @return String the value for this key.
     */
    public String getValue(String id) {
        try {
            if (json != null) {
                JSONArray jsonArray = this.json.getJSONObject("jsonfile")
                        .getJSONArray(EXTRA_PARAMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    if (!id.equals(jsonArray.getJSONObject(i).getString("id")))
                        continue;
                    String value = jsonArray.getJSONObject(i)
                            .getString("value");

                    return value;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send by secure way the CommunicationIntent
     *
     * @param cntx the current Context of app.
     */

    public void sendEvent(Context cntx) {
        try {

            String encryp = SimpleCryptoUtil.encrypt(password, this.params);
            this.putExtra(EXTRA_PARAMS, encryp);
            cntx.sendBroadcast(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
