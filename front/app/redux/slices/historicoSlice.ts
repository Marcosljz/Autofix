import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Historico } from "@/app/types/historico";

interface HistoricoState {
    historicos: Historico[];
}

const initialState: HistoricoState = {
    historicos: []
};

const historicoSlice = createSlice({
    name: "historico",
    initialState,
    reducers: {
        definirHistoricos: (state, action: PayloadAction<Historico[]>) => {
            state.historicos = action.payload;
        },

        limparHistorico: (state) => {
            state.historicos = [];
        }
    }
});

export const { definirHistoricos, limparHistorico } = historicoSlice.actions;

export default historicoSlice.reducer;