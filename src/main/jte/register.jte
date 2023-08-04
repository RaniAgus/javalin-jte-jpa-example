@import static io.github.raniagus.example.enumeration.UserFields.*;

@param io.github.raniagus.example.view.RegisterViewModel vm

@template.utils.form(
title = @`Register`,
styles = @``,
action = @`Create user`,
postUrl = "/register",
children = @`
    <label for="first-name">First name</label>
    <input type="text" id="first-name" name="${FIRST_NAME}"
           placeholder="First name" value="${vm.getFirstName()}" required autofocus>

    <label for="last-name">Last name</label>
    <input type="text" id="last-name" name="${LAST_NAME}"
           placeholder="Last name" value="${vm.getLastName()}" required>

    <label for="email">Email</label>
    <input type="text" id="email" name="${EMAIL}"
           placeholder="Email" value="${vm.getEmail()}" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="${PASSWORD}"
           placeholder="Password" required>

    <label for="confirm-password">Confirm password</label>
    <input type="password" id="confirm-password" name="${CONFIRM_PASSWORD}"
           placeholder="Confirm password" required>

    @for (String error : vm.getErrors())
        <div role="alert">${error}</div>
    @endfor
`,
scripts = @``)
