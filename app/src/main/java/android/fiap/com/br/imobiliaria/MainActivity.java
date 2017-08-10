package android.fiap.com.br.imobiliaria;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button tirarFoto;
    private Button enviarFoto;
    private String caminhoImagem;
    private ImageView imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Pede permissao de usar a camera e escrever em um storage.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        imagem = (ImageView)findViewById(R.id.imgFoto);
    }

        public void tirar(View v){
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// diretório de armazenamento de imagens do dispositivo
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = null;
            try {
// cria o arquivo da imagem
                image = File.createTempFile("foto", ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
// a variável caminho deve ser definida globalmente na Activity pois será utilizada no onActivityResult
            caminhoImagem = image.getAbsolutePath();
// define o arquivo de armazenagem por meio do parâmetro MediaStore.EXTRA_OUTPUT
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
// inicia a câmera
            startActivityForResult(i, 0);
        }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// cria a imagem (Bitmap) a partir do arquivo da imagem
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(caminhoImagem, bmOptions);
        imagem.setImageBitmap(bitmap);
    }

    public void enviar(View v){
        JSONObject imovel = new JSONObject();
        try{
            byte[] val = IOUtils.toByteArray(new FileInputStream(caminhoImagem));
            String code = android.util.Base64.encodeToString(val,android.util.Base64.DEFAULT);
            Log.i("IMOBILIARIA", "Img = " + code);
            imovel.put("proprietario","JOAO");
            imovel.put("imagem",code);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
