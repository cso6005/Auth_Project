import axios from "axios";
import { setCookie, getCookie, deleteCookie } from "../storage/Cookie";

class AuthenticationService {

    loginService(accountEmail, accountPassword) {
        return axios.post("/api/v1/auth/login", {
            accountEmail,
            accountPassword,
        });
    }

    saveTokenSuccessfulLogin(atk, type) {
        sessionStorage.setItem("userType", type);
        
        setCookie("token", atk);
        setCookie("isLogin", true);
    }

}

export default new AuthenticationService();
