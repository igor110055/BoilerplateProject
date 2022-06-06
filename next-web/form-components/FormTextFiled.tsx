import React from "react";
import { Controller } from "react-hook-form";
import {TextField} from '@mui/material'
import { FormInputProps } from "./FormInputProps";
import AccountCircle from '@mui/icons-material/AccountCircle';
import InputAdornment from '@mui/material/InputAdornment';

export const FormTextFiled = ({ name, control,label ,rules,InputProps,sx,readOnly}: FormInputProps) => {
   
  return (
    <Controller
      name={name}
      control={control}
      rules = {rules}
      render={({
        field: { onChange, value },
        fieldState: { error },
        formState,
      }) =>{
          //console.log({value})
          return (
            <TextField label={label}
                        required 
                        size="small"
                        color="secondary" 
                        variant='outlined' 
                        helperText={error ? error.message : null}
                        error={!!error}
                        onChange={onChange}
                        InputProps={InputProps}
                        value={value}
                        fullWidth
                        sx={sx}







            />
          )
      } }
    />
  );
};