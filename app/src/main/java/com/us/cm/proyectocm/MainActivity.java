package com.us.cm.proyectocm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private String url_base="http://mc2018.tk:3000";
    private TextView res;
    private EditText con;
    private TextView URL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(TextView)findViewById(R.id.respuesta);
        con=(EditText)findViewById(R.id.consulta);
        URL=(TextView)findViewById(R.id.url_enviada);

        final Button button = (Button)findViewById(R.id.enviar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                res.setText("");
                try {
                    Socket socket;
                    socket= IO.socket(url_base);
                    String msg=url_base+con.getText().toString();
                    URL.setText(msg);
                    res.append("conectando."+ System.getProperty ("line.separator"));
                    socket.connect();
                    res.append("conectado."+ System.getProperty ("line.separator"));
                    res.append("reciviendo."+ System.getProperty ("line.separator"));
                    socket.on("messages", escucha_msg );
                    res.append("recivido."+ System.getProperty ("line.separator"));
                    res.append("cerrando socket."+ System.getProperty ("line.separator"));
                    socket.disconnect();
                    socket.off();
                    res.append("cerrado socket."+ System.getProperty ("line.separator"));
                } catch (URISyntaxException e) {
                    res.setText(e.getMessage());
                }
            }
        });


    }
    private Emitter.Listener escucha_msg = new Emitter.Listener() {
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String id_msg="";
                String text_msg="";
                id_msg = data.getString("id");
                text_msg = data.getString("text");
                res.append("id: "+id_msg+ System.getProperty ("line.separator"));
                res.append("text: "+text_msg+ System.getProperty ("line.separator"));
            } catch (JSONException e) {
                res.append("error al recibir el mensaje."+ System.getProperty ("line.separator"));
            }
        }
    };
}
