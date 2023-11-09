package com.example.rsu_itcjapp.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rsu_itcjapp.R;

import java.util.ArrayList;

public class ReportesAdapter extends ArrayAdapter<DatosItemReporte> implements View.OnClickListener{

    private Context mContext;
    private ArrayList<DatosItemReporte> datosItemReporte;

    public ReportesAdapter(Context context, ArrayList<DatosItemReporte> datos) {
        super(context, R.layout.row_item_reporte, datos);
        this.mContext = context;
        this.datosItemReporte = datos;
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder {
        TextView twNombre;
        TextView twMatricula;
        TextView twArea;
        TextView twReporte;
    }

    @Override
    public View getView (int posicion, View view, ViewGroup parent) {
        DatosItemReporte item = getItem(posicion);
        ReportesAdapter.ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ReportesAdapter.ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.row_item_reporte, parent, false);
            viewHolder.twNombre = (TextView) view.findViewById(R.id.tw_nombre_alumno);
            viewHolder.twMatricula = (TextView) view.findViewById(R.id.tw_matricula_alumno);
            viewHolder.twArea = (TextView) view.findViewById(R.id.tw_area_alumno);
            viewHolder.twReporte = (TextView) view.findViewById(R.id.tw_reporte_alumno);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ReportesAdapter.ViewHolder) view.getTag();
        }

        viewHolder.twNombre.setText(item.getNombreCompleto());
        viewHolder.twMatricula.setText(item.getMatricula());
        viewHolder.twArea.setText(item.getAreaTrabajo());
        viewHolder.twReporte.setText(item.getReporte());

        return view;
    }

}
