package br.edu.ifsp.admo.sitesfavoritos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.admo.sitesfavoritos.view.SiteAdapter;
import br.edu.ifsp.admo.sitesfavoritos.view.SiteItemClick;


public class MainActivity extends AppCompatActivity implements SiteItemClick {

    private List<Site> datasource;
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new ArrayList<>();

        actionButton = findViewById(R.id.btn_adicionar);
        actionButton.setOnClickListener( v -> novoSite());

        recyclerView = findViewById(R.id.recyclerview);
        SiteAdapter adapter = new SiteAdapter(this, datasource, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void novoSite() {
        LayoutInflater inflater = getLayoutInflater();
        View tela = inflater.inflate(R.layout.novo_site, null);
        EditText apelidoEditText = tela.findViewById(R.id.edittext_apelido);
        EditText urlEditText = tela.findViewById(R.id.edittext_url);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(tela)
                .setTitle(R.string.novo_site)
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.add(
                                new Site(apelidoEditText.getText().toString(),
                                        urlEditText.getText().toString())
                        );
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void clickSiteItem(int position) {
        Site site = datasource.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://" + site.getUrl()));
        startActivity(intent);
    }

    @Override
    public void clickCoracaoSiteItem(int posicao) {
        Site site = datasource.get(posicao);
        site.setFavorito(!site.isFavorito());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void clickLixeiraSiteItem(int posicao) {
        datasource.remove(posicao);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}