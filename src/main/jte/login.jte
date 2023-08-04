@import static io.github.raniagus.example.enumeration.Params.*
@import static io.github.raniagus.example.enumeration.UserFields.*

@param io.github.raniagus.example.view.LoginViewModel vm

@template.utils.form(
title = @`Login`,
styles = @``,
action = @`Login`,
postUrl = "/login",
children = @`
    <input type="hidden" name="${REDIRECT}" value="${vm.getRedirect()}">

    <label for="email">Email</label>
    <input type="text" id="email" name="${EMAIL}" placeholder="Email" value="${vm.getEmail()}" required autofocus>

    <label for="password">Password</label>
    <input type="password" id="password" name="${PASSWORD}" placeholder="Password" required>

    @if(vm.getError() != null)
        <div role="alert">${vm.getError()}</div>
    @endif
`,
scripts = @``)
