@import static io.github.raniagus.example.enumeration.ControllerConstants.*
@import static io.github.raniagus.example.enumeration.UserConstants.*

@param io.github.raniagus.example.view.LoginViewModel vm

@template.utils.form(
title = @`Login`,
styles = @``,
action = @`Login`,
postUrl = "/login",
children = @`
    <input type="hidden" name="${REDIRECT.getValue()}" value="${vm.getRedirect()}">

    <label for="email">Email</label>
    <input type="text" id="email" name="${EMAIL.getValue()}" placeholder="Email" value="${vm.getEmail()}" required autofocus>

    <label for="password">Password</label>
    <input type="password" id="password" name="${PASSWORD.getValue()}" placeholder="Password" required>

    @if(vm.getError() != null)
        <div role="alert">${vm.getError()}</div>
    @endif
`,
scripts = @``)
