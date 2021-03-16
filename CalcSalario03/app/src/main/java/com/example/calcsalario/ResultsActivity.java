package com.example.calcsalario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Button btnVoltar = (Button) findViewById(R.id.btnRetornar);

        Intent intent = getIntent();

        double salariobruto = intent.getDoubleExtra(MainActivity.SALARIO_BRUTO, 0);
        int dependentes = intent.getIntExtra(MainActivity.DEPENDENTES, 0);
        double descontos = intent.getDoubleExtra(MainActivity.DESCONTOS, 0);

        buildLayout(salariobruto, dependentes, descontos);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });


    }

    private void buildLayout(double salariobruto, int dependentes, double descontos) {

        TextView txtSalarioBruto = (TextView) findViewById(R.id.txtSalarioBruto);
        TextView txtINSS = (TextView) findViewById(R.id.txtINSS);
        TextView txtIRRF = (TextView) findViewById(R.id.txtIRRF);
        TextView txtDescontos = (TextView) findViewById(R.id.txtDescontos);
        TextView txtSalarioLiquido = (TextView) findViewById(R.id.txtSalarioLiquido);
        TextView txtPercentualDescontos = (TextView) findViewById(R.id.txtPercentualDescontos);

        double contribuicaoINSS = calculaINSS(salariobruto);

        double baseCalculo = salariobruto - contribuicaoINSS - (dependentes * 189.59);

        double contribuicaoIRRF = calculaIRRF(baseCalculo);

        double salarioLiquido = salariobruto - contribuicaoINSS - contribuicaoIRRF - descontos;

        double percentualDescontos = (1 - salarioLiquido / salariobruto) * 100;


        txtSalarioBruto.setText(String.valueOf(formatDouble(salariobruto)));
        txtINSS.setText(String.valueOf(formatDouble(contribuicaoINSS*-1)));
        txtIRRF.setText(String.valueOf(formatDouble(contribuicaoIRRF*-1)));
        txtDescontos.setText(String.valueOf(formatDouble(descontos*-1)));
        txtSalarioLiquido.setText(String.valueOf(formatDouble(salarioLiquido)));
        txtPercentualDescontos.setText(String.valueOf(formatDouble(percentualDescontos))+"%");

    }

    private double formatDouble(double d){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.valueOf(decimalFormat.format(d));
    }

    private double calculaIRRF(double baseCalculo){

        if (baseCalculo <= 1903.98)
            return 0;

        if (baseCalculo <= 2826.65)
            return (baseCalculo * 0.075) - 142.80;

        if (baseCalculo <= 3751.05)
            return (baseCalculo * 0.15) - 354.80;

        if (baseCalculo <= 4664.68)
            return (baseCalculo * 0.225) - 636.13;

        return (baseCalculo*0.275) - 869.36;
    }



    private double calculaINSS(double salariobruto) {

        if (salariobruto <= 1045)
            return salariobruto * 0.075;
        if (salariobruto <= 2089.60)

            return (salariobruto * 0.09) - 15.67;

        if (salariobruto <= 3134.00)
            return (salariobruto * 0.12) - 78.36;

        if (salariobruto <= 6101.06)
            return (salariobruto * 0.14) - 141.05;
        return 713.10;
    }
}