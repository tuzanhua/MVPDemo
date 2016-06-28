package com.susu.gradlecommanddemo;

/**
 * 作者：suxianming on 2016/4/27 15:42
 */
public class PresenterImpl implements LoginPresenter,LoginInteractor.OnFinishLogin {
    private LoginView loginView;
    private LoginInteractor loginInteractor;
    public PresenterImpl(LoginView loginView){
        this.loginView = loginView;

    }
    @Override
    public void validateUser(String username, String password) {
        if(loginInteractor==null){
            loginInteractor = new LoginInteractorImpl();
        }
        loginInteractor.login(username,password,this);
        loginView.showProgress();
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if(loginView!=null){
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if(loginView!=null){
            loginView.setPwdError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if(loginView!=null){
            loginView.navigateToHome();
            loginView.hideProgress();
        }
    }
}
