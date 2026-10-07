package com.bnb_prueba_tecnica_mauricio_apaza.ui.autenticacion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bnb_prueba_tecnica_mauricio_apaza.databinding.ItemConsejoBinding;

import java.util.Collections;
import java.util.List;

public final class ConsejosAdapter extends RecyclerView.Adapter<ConsejosAdapter.ConsejoViewHolder> {

    private final List<Consejo> consejos;

    public ConsejosAdapter(List<Consejo> consejos) {
        this.consejos = Collections.unmodifiableList(consejos);
    }

    @NonNull
    @Override
    public ConsejoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConsejoBinding binding = ItemConsejoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ConsejoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsejoViewHolder holder, int position) {
        holder.bind(consejos.get(position));
    }

    @Override
    public int getItemCount() {
        return consejos.size();
    }

    static final class ConsejoViewHolder extends RecyclerView.ViewHolder {

        private final ItemConsejoBinding binding;

        ConsejoViewHolder(ItemConsejoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Consejo consejo) {
            binding.tvConsejo.setText(consejo.getTexto());
            binding.ivIlustracion.setImageResource(consejo.getIlustracion());
        }
    }
}
