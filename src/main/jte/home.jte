@param io.github.raniagus.example.view.HomeViewModel vm

@template.base(
title = @`Home`,
styles = @``,
body = @`
    <body>
        <main class="container">
            <section class="row">
                <div class="col-md-12">
                    <h1>Hello, ${vm.getFirstName()}!</h1>
                </div>
            </section>
            <section class="row">
                <div class="col-md-12">
                    @if(vm.isAdmin())
                        <a class="btn btn-primary" href="/register">
                            Register User
                        </a>
                    @endif
                    <form method="post" action="/logout">
                        <button class="btn btn-secondary">
                            Logout
                        </button>
                    </form>
                </div>
            </section>
        </main>
    </body>
`,
scripts = @``)
