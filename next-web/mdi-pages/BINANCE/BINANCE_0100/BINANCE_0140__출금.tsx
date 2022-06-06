import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {MenuContext} from "@/store/MenuStore";
import Button from "@mui/material/Button";
import { FormTextFiled } from "@/form-components/FormTextFiled";
import { useForm } from "react-hook-form";

interface IFormInput {
    COIN:string;
    WITHDRAW_ORDER_ID:string;
    NETWORK:string;
    ADDRESS:string;
    ADDRESS_TAG:string;
    AMOUNT:string;
    TRANSACTION_FEE_FLAG:string;
    NAME:string;
}

const defaultValues = {
    COIN:"",
    WITHDRAW_ORDER_ID:"",
    NETWORK:"",
    ADDRESS:"",
    ADDRESS_TAG:"",
    AMOUNT:"",
    TRANSACTION_FEE_FLAG:"",
    NAME:""
};


export const BINANCE_0140__출금= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

    const fnWithdrawHandler= async (data: IFormInput) => {
       
        messageConfirm("출금하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_BINANCE_WALLET_POST_SAPI_V1_Withdraw',{
                brRq 		: 'IN_PSET'
                ,brRs 		: 'OUT_RSET'
                ,IN_PSET : [ data ]
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("출금하였습니다",function()  {
                        if (data) {
                            console.log(data);
                        }
                    });
                }
            })                      
        })
    }


    return (
            <>
               <pre className="tal lh12">
                    <h3>Withdraw(USER_DATA)</h3>
                    POST /sapi/v1/capital/withdraw/apply (HMAC SHA256)
                    Submit a withdraw request.
                    <b>Weight(IP):</b>  1
                </pre>
                <hr />
                <pre className="tal lh12">
                    <ul>
                        <li>If network not send, return with default network of the coin.</li>
                        <li>You can get network and isDefault in networkList of a coin in the response of Get /sapi/v1/capital/config/getall (HMAC SHA256).</li> 
                    </ul>
                </pre>
                <FormTextFiled  name="COIN"  control={control}  label="COIN(필수)"  rules={{ required: `COIN required` }} /> 
                <FormTextFiled  name="WITHDRAW_ORDER_ID"  control={control}  label="WITHDRAW_ORDER_ID(client id for withdraw)"  /> 
                <FormTextFiled  name="NETWORK"  control={control}  label="NETWORK"  />
                <FormTextFiled  name="ADDRESS"  control={control}  label="ADDRESS(필수)"  rules={{ required: `ADDRESS required` }} /> 
                <FormTextFiled  name="ADDRESS_TAG"  control={control}  label="ADDRESS_TAG(Secondary address identifier for coins like XRP,XMR etc.)"   /> 
                <FormTextFiled  name="AMOUNT"  control={control}  label="AMOUNT(필수)"    rules={{ required: `AMOUNT(필수) required` }} /> 
                <FormTextFiled  name="TRANSACTION_FEE_FLAG"  control={control}  label="TRANSACTION_FEE_FLAG"   /> 
                (	When making internal transfer, true for returning the fee to the destination account; false for returning the fee back to the departure account. Default false.)
                <FormTextFiled  name="NAME"  control={control}  label="NAME"   /> 
                (Description of the address. Space in name should be encoded into %20.)                
                <div style={{padding: 10}}>
                <Button variant="contained" color="success"  onClick={handleSubmit(fnWithdrawHandler)}>출금</Button>
                </div>
            </>
    )
  }
