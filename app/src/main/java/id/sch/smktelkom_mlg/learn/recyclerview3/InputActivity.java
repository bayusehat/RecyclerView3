package id.sch.smktelkom_mlg.learn.recyclerview3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InputActivity extends AppCompatActivity {
    public static final String HOTEL = "hotel";
    static final int REQUEST_IMAGE_GET = 1;
    EditText etJudul;
    EditText etDeskripsi;
    EditText etDetail;
    EditText etLokasi;
    ImageView ivFoto;
    Uri uriFoto;
    Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        etJudul = (EditText) findViewById(R.id.editTextNama);
        etDeskripsi = (EditText) findViewById(R.id.editTextDeskripsi);
        etDetail = (EditText) findViewById(R.id.editTextDetail);
        etLokasi = (EditText) findViewById(R.id.editTextLokasi);
        ivFoto = (ImageView) findViewById(R.id.imageViewFoto);

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });

        findViewById(R.id.buttonSimpan).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSave();
                    }
                });
    }

    private void doSave(){
        String judul = etJudul.getText().toString();
        String deskripsi = etDeskripsi.getText().toString();
        String detail = etDetail.getText().toString();
        String lokasi = etLokasi.getText().toString();

        if(isValid(judul,deskripsi,detail,lokasi,uriFoto))
        {
            hotel = new Hotel(judul,deskripsi,detail,lokasi,uriFoto.toString());
            Intent intent = new Intent();
            intent.putExtra(MainActivity.HOTEL,hotel);
            setResult(RESULT_OK,intent);
            finish();


        }
    }

    private boolean isValid(String judul, String deskripsi, String detail, String lokasi, Uri uriFoto)
    {
        boolean valid = true;
        if(judul.isEmpty())
        {
            setErrorEmpty(etJudul);
            valid = false;
        }
        if(deskripsi.isEmpty())
        {
            seErrorEmpty(etDeskripsi);
            valid=false;
        }
        if(detail.isEmpty())
        {
            setErrorEmpty(etDetail);
            valid=false;
        }
        if(lokasi.isEmpty())
        {
            setErrorEmpty(etLokasi);
            valid=false;
        }
        if(uriFoto ==null)
        {
            Snackbar.make(ivFoto,"Foto Belum Ada",Snackbar.LENGTH_SHORT)
                    .show();
            valid=false;
        }
        return valid;
    }
    private void pickPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQUEST_IMAGE_GET);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)
        {
            uriFoto=data.getData();
            ivFoto.setImageURI(uriFoto);
        }
    }

}
