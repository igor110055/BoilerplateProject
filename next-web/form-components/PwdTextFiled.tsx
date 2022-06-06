import React from "react";
import { Controller } from "react-hook-form";
import {TextField} from '@mui/material'
import { FormInputProps } from "./FormInputProps";
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import InputAdornment from '@mui/material/InputAdornment';

export const PwdTextFiled = ({ name, control,label,rules }: FormInputProps) => {
  return (
    <Controller
      name={name}
      control={control}
      rules = {rules}
      render={({
        field: { onChange, value },
        fieldState: { error },
        formState,
      }) => (
        <TextField label={label}
                                    required 
                                    size="medium" 
                                    color="secondary" 
                                    variant='outlined' 
                                    InputProps={{
                                        startAdornment: (
                                          <InputAdornment position="start">
                                            <VpnKeyIcon />
                                          </InputAdornment>
                                        ),
                                      }}  
                                    helperText={error ? error.message : null}
                                    error={!!error}
                                    onChange={onChange}
                                    value={value}
                                    fullWidth
                                    type="password"                                                                        
                        />
      )}
    />
  );
};