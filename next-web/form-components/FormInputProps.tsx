import { SxProps, Theme } from "@mui/material";

export interface FormInputProps {
    name: string;
    control: any;
    label?: string;
    setValue?: any;
    rules?:any;
    InputProps?:any;
    options?: any
    readOnly?:boolean;
    sx?: SxProps<Theme> 
  }