import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice";
import ordemServicoReducer from "./slices/ordemServicoSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    ordensServico: ordemServicoReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
