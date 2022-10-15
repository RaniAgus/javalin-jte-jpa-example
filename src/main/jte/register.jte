@param io.github.raniagus.project.view.RegisterViewModel vm

@template.user-form(
title = @`Register`,
styles = @``,
error = vm.getError(),
postUrl = "/register",
children = @`
    <label for="confirm-password" class="sr-only">Confirm password</label>
    <input class="form-control" type="password" id="confirm-password" name="confirm-password"
           placeholder="Confirm password" required>
`,
scripts = @``)
