#!/usr/bin/rakudo

for (@*ARGS) -> $input-filename {
    my @contents = $input-filename.IO.lines;

    my @imports;
    # Find imports
    for (@contents) {
        if (m:s/^^ "import" [ \w+ "." ] * ( \w+ ) $$/) {
            push @imports, $0.Str;
        }
    }

    # Find which ones are used
    my SetHash $unused-imports;
    for (@imports) -> $name {
        my $found = False;
        for (@contents) -> $line {
            next if $line ~~ /import/;
            if ($line ~~ /«$name»/) {
                $found = True;
                last;
            }
        }
        if (!$found) {
            $unused-imports{$name} = 1;
            say "Unused import $name in $input-filename";
        }
    }

    @contents = @contents.grep(-> $line {
        !($line ~~ m:s/^^ "import" .* "." ( \w+ ) $$/) ||
        $0.Str ∉ $unused-imports
    });

    spurt $input-filename, @contents.join("\n") ~ "\n";

}
