import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {BrowserRouter} from "react-router-dom";
import {ThemeProvider} from "@mui/material";
import theme from "./theme/theme.ts";
import ToasterProvider from "./ToasterContext.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <BrowserRouter>
          <ThemeProvider theme={theme}>
              <ToasterProvider>
                <App />
              </ToasterProvider>
          </ThemeProvider>
      </BrowserRouter>
  </StrictMode>
)
