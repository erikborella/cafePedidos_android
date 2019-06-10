package com.example.cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private class ViewHolder {
        public RadioGroup cafesRGroup = findViewById(R.id.cafes_rGroup);

        public RadioButton cafeRadio = findViewById(R.id.cafe_radio);
        public RadioButton cafeComLeiteRadio = findViewById(R.id.cafe_com_leite_radio);
        public RadioButton cappucinoRadio = findViewById(R.id.cappucino_radio);

        public TextView quantityContText = findViewById(R.id.quantity_cont_text);
        public TextView unitPriceText = findViewById(R.id.unit_price_text);
        public TextView totalPriceText = findViewById(R.id.total_price_text);

        public TextView pedidoText = findViewById(R.id.pedido_text);

        public Button sendButton = findViewById(R.id.send_button);
    }

    private enum CafesPrice {
        NOTHING(0),
        CAFE(2.0),
        CAFE_COM_LEITE(2.5),
        CAPPUCINO(5.0);

        private double price;

        CafesPrice(double price) {
            this.price = price;
        }
    }

    private ViewHolder viewHolder;
    private CafesPrice actualCafe = CafesPrice.NOTHING;

    private int quantityCont = 0;
    private double totalPrice = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewHolder = new ViewHolder();
        this.update();
    }

    public void onPlusButtonClikc(View view) {
        this.quantityCont++;
        this.update();
    }

    public void onMinusButtonClick(View view) {
        if (this.quantityCont > 0) {
            this.quantityCont--;
        }
        this.update();
    }

    public void onCafeRadioClick(View view) {
        switch (viewHolder.cafesRGroup.getCheckedRadioButtonId()) {
            case R.id.cafe_radio:
                this.actualCafe = CafesPrice.CAFE;
                break;
            case R.id.cafe_com_leite_radio:
                this.actualCafe = CafesPrice.CAFE_COM_LEITE;
                break;
            case R.id.cappucino_radio:
                this.actualCafe = CafesPrice.CAPPUCINO;
        }

        this.update();
    }

    public void onSendPedidoClick(View view) {
        Intent sendMail = new Intent(Intent.ACTION_SEND);
        sendMail.putExtra(Intent.EXTRA_EMAIL, "cafe.do.ifc.concordia@gmail.com");
        sendMail.putExtra(Intent.EXTRA_TEXT, String.valueOf(viewHolder.pedidoText.getText()));
        sendMail.putExtra(Intent.EXTRA_SUBJECT, "Cafe");

        if (sendMail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendMail);
        }

    }

    private void update() {
        double totalPrice = this.actualCafe.price * this.quantityCont;

        viewHolder.quantityContText.setText(String.valueOf(this.quantityCont));

        viewHolder.unitPriceText.setText(String.format(
                getString(R.string.preco_unitario_layout),
                this.actualCafe.price
        ));

        viewHolder.totalPriceText.setText(String.format(
                getString(R.string.preco_total_layout),
                totalPrice
        ));

        if (this.actualCafe == CafesPrice.NOTHING) {
            viewHolder.pedidoText.setText(getText(R.string.selecione_um_tipo_de_cafe_primeiro));
            viewHolder.sendButton.setEnabled(false);
        } else {
            viewHolder.pedidoText.setText(String.format(
                    getString(R.string.pedido_layout),
                    this.quantityCont,
                    (this.quantityCont == 1) ? getString(R.string.cafe):getString(R.string.cafes),
                    totalPrice
            ));
            viewHolder.sendButton.setEnabled(true);
        }

    }
}
