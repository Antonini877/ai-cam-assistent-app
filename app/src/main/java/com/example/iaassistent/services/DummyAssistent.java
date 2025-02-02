package com.example.iaassistent.services;

public class DummyAssistent {

    private int index = 0;
    private int max = 1;
    private String[] results  = {
        "Você pode fazer uma salada de alcachofra com queijo e folhas verdes! Aqui está a receita:\n" +
        "\n" +
        "Preparar as Alcachofras: Se estiver usando alcachofras em conserva, escorra e enxágue. Se forem frescas, cozinhe-as até ficarem macias e depois corte em pedaços.\n" +
        "\n" +
        "Montar a Salada: Em uma tigela grande, coloque as folhas de alface e a rúcula.\n" +
        "\n" +
        "Adicionar os Ingredientes: Acrescente as alcachofras cortadas e o queijo esfarelado ou em cubos.\n" +
        "\n" +
        "Temperar: Regue com azeite e tempere com sal a gosto. Misture delicadamente para não quebrar as alcachofras e o queijo.\n" +
        "\n" +
        "Servir: Sirva imediatamente como entrada ou acompanhamento.",

        "Aqui está uma receita simples e deliciosa de torradas com queijo e geleia, acompanhadas de café:\n" +
                "\n" +
        "Modo de Preparo:\n" +
        "Preparar o Pão: Se desejar, passe uma fina camada de manteiga nas fatias de pão. Isso ajudará a deixá-las mais douradas e saborosas ao serem tostadas.\n" +
        "\n" +
        "Tostar o Pão:\n" +
        "\n" +
        "Na Frigideira: Aqueça uma frigideira em fogo médio e coloque as fatias de pão. Toste de cada lado até ficarem douradas.\n" +
        "Na Torradeira: Se preferir, você pode usar uma torradeira para torrar o pão.\n" +
        "Adicionar o Queijo:\n" +
        "\n" +
        "Após torrar, coloque uma fatia de queijo em cima de cada fatia de pão e deixe derreter um pouco. Se estiver usando a frigideira, você pode cobrir a frigideira com uma tampa para ajudar o queijo a derreter.\n" +
        "Adicionar a Geleia: Quando o queijo estiver derretido, retire o pão da frigideira ou torradeira e espalhe uma colher de sopa de geleia sobre cada fatia.\n" +
        "\n" +
        "Servir: Sirva as torradas quentes acompanhadas de uma xícara de café."


    };
    public String call(){
        String s = this.results[this.index];
        if(this.index < this.max){
            this.index += 1;
        }else{
            this.index = 0;
        }

        return s;

    }
}
