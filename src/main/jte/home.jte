@param io.github.raniagus.project.view.HomeViewModel vm

@template.base(
title = @`Home`,
styles = @``,
body = @`
    <body>
        <main class="container">
            <section class="row">
                <div class="col-md-12">
                    <h1>Hello, ${vm.getUsername()}!</h1>
                </div>
            </section>
            <section class="row">
                <div class="col-md-12">
                    @if(vm.isAdmin())
                        <button class="btn btn-primary" onclick="window.location.href = '/register'">
                            Register User
                        </button>
                    @endif
                    <button class="btn btn-secondary" onclick="window.location.href='/logout'">
                        Logout
                    </button>
                </div>
            </section>
        </main>
    </body>
`,
scripts = @``)
