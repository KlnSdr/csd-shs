function startup() {
    edom.init();
    renderDefaultUi();
    checkPhase();
}

function renderDefaultUi() {
    edom.fromTemplate({
        children: [
            {
                tag: 'nav',
                id: 'navbar',
            },
            {
                tag: 'div',
                classes: ['content'],
                children: [
                    {
                        tag: 'div',
                        classes: ['buttonBar'],
                        id: 'tabBar',
                        children: [
                            {
                                tag: 'button',
                                classes: ['primaryButton'],
                                text: 'Schüler*innen',
                                id: 'bttnOpenStudents',
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'clickOpenStudents',
                                        arguments: '',
                                        body: 'openTabStudents()',
                                    },
                                ],
                            },
                            {
                                tag: 'button',
                                classes: ['secondaryButton'],
                                text: 'Einstellungen',
                                id: 'bttnOpenSettings',
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'clickOpenSettings',
                                        arguments: '',
                                        body: 'openTabSettings()',
                                    },
                                ],
                            },
                        ],
                    },
                    { tag: 'div', id: 'content' },
                ],
            },

            {
                tag: 'footer',
                id: 'footer',
            },
        ],
    });
}

function resetTabs() {
    const container: edomElement = edom.findById('content') as edomElement;

    while (container.children.length > 0) {
        container.clear();
    }

    edom.findById('tabBar')?.children.forEach((c: edomElement) => {
        c.swapStyle('primaryButton', 'secondaryButton');
    });
}

function openTabStudents() {
    resetTabs();
    edom.findById('bttnOpenStudents')?.swapStyle(
        'secondaryButton',
        'primaryButton'
    );
    checkPhase();
}

function openTabSettings() {
    resetTabs();
    edom.findById('bttnOpenSettings')?.swapStyle(
        'secondaryButton',
        'primaryButton'
    );
    settingsUI();
}

function checkPhase() {
    fetch(`${backend}/api/shs/admin/is-phase-two`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${response.statusText} - ${response.text()}`
                );
            }
            return response.json();
        })
        .then((isPhase2: boolean) => {
            if (isPhase2) {
                renderPhase2Ui();
            } else {
                renderPhase1Ui();
            }
        })
        .catch((e: any) => {
            // TODO
            console.error(e);
        });
}
