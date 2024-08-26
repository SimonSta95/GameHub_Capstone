import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {BrowserRouter} from "react-router-dom";
import {ThemeProvider} from "@mui/material";
import theme from "./theme/theme.ts";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <BrowserRouter>
          <ThemeProvider theme={theme}>
            <App />
          </ThemeProvider>
      </BrowserRouter>
  </StrictMode>
)
