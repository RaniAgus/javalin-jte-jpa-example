@param io.github.raniagus.example.view.RegisterViewModel vm

@template.utils.form(
title = @`Register`,
styles = @``,
action = @`Create user`,
postUrl = "/register",
children = @`
    <label for="first-name" class="sr-only">First name</label>
    <input class="form-control" type="text" id="first-name" name="first-name"
           placeholder="First name" value="${vm.getFirstName()} "required autofocus>

    <label for="last-name" class="sr-only">Last name</label>
    <input class="form-control" type="text" id="last-name" name="last-name"
           placeholder="Last name" value="${vm.getLastName()}" required>

    <label for="email" class="sr-only">Email</label>
    <input class="form-control" type="text" id="email" name="email"
           placeholder="Email" value="${vm.getEmail()}" required>

    <label for="password" class="sr-only">Password</label>
    <input class="form-control" type="password" id="password" name="password"
           placeholder="Password" required>

    <label for="confirm-password" class="sr-only">Confirm password</label>
    <input class="form-control" type="password" id="confirm-password" name="confirm-password"
           placeholder="Confirm password" required>

    @for (String error : vm.getErrors())
        <div class="alert alert-danger" role="alert">${error}</div>
    @endfor
`,
scripts = @``)
