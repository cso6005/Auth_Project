import {React, useState} from "react";
import { useNavigate } from "react-router-dom";

import AuthenticationService from "../../service/AuthenticationService";


export default function LoginComponent(props) {

    const [state, setState] = useState({
        accountEmail:"",
        accountPassword:"",
    });

    const handleChange = (event) => {
        setState({
          ...state,
          [event.target.name]: event.target.value,
        });
    };

    const loginClicked = () => {
        AuthenticationService.loginService(
            state.accountEmail,
            state.accountPassword
        ).then((response) => {
            AuthenticationService.saveTokenSuccessfulLogin(
              response.data.atk,
              response.data.accountType
            );
            setState({ hasLoginFailed: true });
        }).catch(() => {
            alert("회원 정보를 확인해주세요.");
        });
    };


return (
    <div className="Login">
        <h3>Login</h3>
        <input
            type="text"
            name="accountEmail"
            placeholder="Email"
            value={state.accountEmail}
            onChange={handleChange}
          />

        <input
            type="password"
            name="accountPassword"
            placeholder="Password"
            value={state.accountPassword}
            onChange={handleChange}
        />

        <button onClick={loginClicked}>
            Sign in
        </button>



    </div>
);


};

