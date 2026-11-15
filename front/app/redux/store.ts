import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/slices/authSlice";
import historicoReducer from "../redux/slices/historicoSlice";

export const store = configureStore({

    reducer: {
        auth: authReducer,
        historico: historicoReducer
    }

});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;