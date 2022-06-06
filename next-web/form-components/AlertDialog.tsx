import {forwardRef,useState,useEffect,useImperativeHandle,useRef} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
  
export type AlertDialogHandler = {
    setValue:(value:string, fn?:any)=>void;
};

const AlertDialog = forwardRef<AlertDialogHandler,any>((props,ref)=> {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState("");
  const localFn = useRef<Function>();

  
  useImperativeHandle(ref, () => ({
    setValue(value:string, fn:any) {
        console.log("AlertDialog");
        setOpen(true);
        setValue(value);
        localFn.current=fn;
    }
  }))

  const handleClose = () => {
    setOpen(false);
    if(localFn.current){
        //console.log("aaaaaaaaaa")
        localFn.current();
    }
  };

  return (
      <Dialog
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"

        sx={{ '& .MuiDialog-paper': { width: '80%', maxHeight: 435 } }}
        maxWidth="xs"
        open={open}

      >
        <DialogTitle id="alert-dialog-title">
          경고
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {value}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
  );
});


export default AlertDialog;