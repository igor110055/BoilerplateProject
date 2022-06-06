import React from "react";
import { Controller } from "react-hook-form";
import {TextField} from '@mui/material'
import { FormInputProps } from "./FormInputProps";
import AccountCircle from '@mui/icons-material/AccountCircle';
import InputAdornment from '@mui/material/InputAdornment';
import MailIcon from '@mui/icons-material/Mail'

export const EmailTextFiled = ({ name, control,label,rules,readOnly }: FormInputProps) => {

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
          <>
        <TextField label={label} 
                                    required 
                                    size="small"
                                    color="secondary" 
                                    variant='outlined'
                                    InputProps={{
                                        startAdornment: (
                                        <InputAdornment position="start">
                                            <MailIcon />
                                        </InputAdornment>
                                        ),
                                        readOnly:readOnly
                                    }}
                                    helperText={error ? error.message : null}
                                    error={!!error}
                                    onChange={onChange}
                                    value={value}
                                    fullWidth
                                    
                        />
          </>
        
      )}
    />
  );
};