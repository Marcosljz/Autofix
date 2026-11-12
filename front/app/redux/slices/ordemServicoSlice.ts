import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { OrdemServico } from "@/app/types/ordemservico";

interface OrdemServicoState {
  lista: OrdemServico[];
}

const initialState: OrdemServicoState = {
  lista: [],
};

const ordemServicoSlice = createSlice({
  name: "ordensServico",
  initialState,
  reducers: {
    definirOrdensServico: (state, action: PayloadAction<OrdemServico[]>) => {
      state.lista = action.payload;
    },
    adicionarOrdemServico: (state, action: PayloadAction<OrdemServico>) => {
      const indice = state.lista.findIndex((item) => item.id === action.payload.id);
      if (indice >= 0) {
        state.lista[indice] = action.payload;
      } else {
        state.lista.push(action.payload);
      }
    },
    removerOrdemServico: (state, action: PayloadAction<number>) => {
      state.lista = state.lista.filter((item) => item.id !== action.payload);
    },
  },
});

export const {
  definirOrdensServico,
  adicionarOrdemServico,
  removerOrdemServico,
} = ordemServicoSlice.actions;

export default ordemServicoSlice.reducer;
